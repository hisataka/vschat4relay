$(document).ready(function(){
  makeGameList();

  $(document).on('click', '.gamelist', function() {
    var row = $(this).closest('tr').index();
    var game_id = $('#runninggametable')[0].rows[row + 1].cells[1].innerHTML;
    window.location.href = "newgame.html?game_id=" + game_id;
  });
});

// ゲーム一覧を作成
function makeGameList() {
  $.ajax({
    type: 'GET',
    url: '/gamelist',
    dataType: 'json',
    success: function(json){
      var len = json.length;
      var list = "";

      for (var i = 0; i < len; i ++) {
        list += "<tr style='cursor:pointer'><td class='gamelist'>" + i + "</td>";
        list += "<td class='gamelist'>" + json[i].id + "</td>";
        list += "<td class='gamelist'>" + json[i]['goal-word'] + "</td>";
        list += "<td class='gamelist'>" + json[i]['curr-word'] + "</td></tr>";
      }

      $('#appendElem').append(list);
    }
  });
}

function deleteLog(gameId) {
  var url = "/delete?game_id=" + gameId;
  $.ajax({
    type: 'GET',
    url: url
  });
}

// ログエリア更新
function updateLog(gameId) {
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
