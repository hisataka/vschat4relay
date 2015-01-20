// 実行中のゲームID
var gameId = "";
// タイマー
var timer;

$(document).ready(function(){
  $('#pauseButton').css("cursor","pointer");
  $('#backButton').css("cursor","pointer");
  $('.errmsg').hide();
  $(document).on('click', '#backButton', function() {
    window.location.href = "newgame.html";
  });
  // 開始画面要素生成
  makeStartElem($('#appendStartElem'));
  $('#backButton').hide();

  // ゲーム開始処理
  $(document).on('click', '#startButton', startGame);

  // ゲーム停止処理
  $(document).on('click', '#pauseButton', stopGame);

  $(document).on('click', '#chatButton', chat);

  // パラメータ
  var params = getUrlVars();
  if (params['game_id']) {
    // 継続ゲーム
    $('#startElem').hide();
    gameId = params['game_id'];
    timer = setInterval(reload,2000);
  } else {
    // 新しいゲーム
    $('#gameElem').hide();
  }
});

function getUrlVars()
{
  var vars = [], hash;
  var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
  for(var i = 0; i < hashes.length; i++) {
    hash = hashes[i].split('=');
    vars.push(hash[0]);
    vars[hash[0]] = hash[1];
  }
  return vars;
}

// chat乱入
function chat() {
  var url = "/chat?game_id=" + gameId + "&word=" + $('#chat').val();
  $.ajax({
    type: 'GET',
    url: url,
    dataType: 'json',
    success: function(json){
      $('#chat').val("");
    }
  });
}

// ゲーム終了
function stopGame() {
  var url = "/stop?game_id=" + gameId;
  $.ajax({
    type: 'GET',
    url: url,
    dataType: 'json',
    success: function(json){
      // タイマー解除
      clearInterval(timer);

      // ボタンの表示/非表示
      $('#running').hide();
      $('#backButton').show();
    }
  });
}

// ゲーム中画面リロード
function reload() {
  var url = "/log?game_id=" + gameId;
  $.ajax({
    type: 'GET',
    url: url,
    dataType: 'json',
    success: function(json){
      var len = json.length;
      var log = "<p>";
      for (var i = 0; i < len; i ++) {
          log += "<div class='col-sm-1'>";
          log += "<img src='" + json[i].picture_url + "' width='100px' height='100px' />";
          log += "</div><div class='col-sm-11'><div class='comment balloon-left'><p>" + json[i].word + "</p></div></div>";
      }
      $('#logElem').empty();
      $('#logElem').append(log);
    }
  });
}

// ゲーム開始
function startGame() {
  var bot1 = $(':radio[name="bot_id1"]:checked').val();
  var bot2 = $(':radio[name="bot_id2"]:checked').val();
  var start = $(':text[name="start"]').val();
  var goal = $(':text[name="goal"]').val();

  // 入力チェック
  if (!(start && goal)) {
    $('.errmsg').text('開始・終了ワードを設定してください').show();
    return ;
  }

  var url;
  if (bot1 && bot2) {
    // 1 vs 1
    url = "/start?bot_id1=" + bot1 + "&bot_id2=" + bot2 + "&start=" + start + "&goal=" + goal;
  } else {
    // マルチbotゲーム
    url = "/multi?start=" + start + "&goal=" + goal;
  }

  // 要素表示/非表示
  $('.errmsg').text('').hide();
  $('#startElem').hide();
  $('#gameElem').show();

  // 実行
  $.ajax({
    type: 'GET',
    url: url,
    dataType: 'json',
    success: function(json){
      gameId = json.game_id;
      timer = setInterval(reload,2000);
    }
  });
}

// 開始画面生成
function makeStartElem(elem) {
  $.ajax({
    type: 'GET',
    url: '/botlist',
    dataType: 'json',
    success: function(json){
      var len = json.length;
      var appendElemStr = "<div class='col-sm-12' ><h2>Player 1?</h2><div class='btn-group' data-toggle='buttons'>";
      for (var i = 0; i < len; i ++){
        appendElemStr += "<label class='btn btn-primary btn-lg'>" +
          "<input type='radio' name='bot_id1' id='bot_id1_" + i + "' value='" + json[i].bot_id + "'>" + json[i].bot_name + "</label>";
      }
      appendElemStr += "</div></div>";
      appendElemStr += "<div class='col-sm-12' ><h2>Player 2?</h2><div class='btn-group' data-toggle='buttons'>";
      for (var i = 0; i < len; i ++){
        appendElemStr += "<label class='btn btn-primary btn-lg'>" +
          "<input type='radio' name='bot_id2' id='bot_id2_" + i + "' value='" + json[i].bot_id + "'>" + json[i].bot_name + "</label>";
      }
      appendElemStr += "</div></div>";

      appendElemStr += "<div class='col-sm-12'><h2>Start-word?</h2><input type='text' name='start' /></div>";
      appendElemStr += "<div class='col-sm-12'><h2>Goal-word?</h2><input type='text' name='goal' /></div>";
      appendElemStr += "<div class='col-sm-12 col-xs-10 col-xs-offset-1' style='margin-top:1em;'><img id='startButton' src='img/start.png' width='50px' height='50px'/></div>"
      $(elem).append(appendElemStr);

      $('#startButton').css("cursor","pointer");
    }
  });
}
