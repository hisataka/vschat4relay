$(document).ready(function(){
  makeGameList();

  $(document).on('click', '.gamelist', function() {
    var row = $(this).closest('tr').index();
    var game_id = $('#oldgametable')[0].rows[row + 1].cells[1].innerHTML;
    updateLog(game_id);
  });

  $(document).on('click', '.delete', function() {
    var row = $(this).closest('tr').index();
    var game_id = $('#oldgametable')[0].rows[row + 1].cells[1].innerHTML;

    $('#oldgametable')[0].deleteRow(row + 1);
    $('#logElem').empty();
    deleteLog(game_id);
  });
});

// ゲーム一覧を作成
function makeGameList() {
  $.ajax({
    type: 'GET',
    url: '/oldgamelist',
    dataType: 'json',
    success: function(json){
      var len = json.length;
      var list = "";
      for (var i = 0; i < len; i ++) {
        list += "<tr style='cursor:pointer'><td class='gamelist'>" + i + "</td><td class='gamelist'>" + json[i].game_id + "</td><td class='gamelist'>" + json[i].count + "</td><td class='gamelist'>" + json[i].ins_time + "</td>";
        list += "<td class='delete'><button type='button' class='btn btn-default'>delete</button></td></tr>";
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
