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
