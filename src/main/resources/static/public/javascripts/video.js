  var getData = (kind) => {
    return callApi('GET', '/' + kind ,{kind});
  };
  
  $(document).ready( function (){
    var data=getData(kind);
    var tbody=('<tbody></tbody>')
    for(var i=1;i<data.length;i++) {
      var td = ('<td></td>')
      td.append('<tr>' + data.idx + '</tr>')
      td.append('<tr>' + data.name + '</tr>')
      td.append('<tr>' + data.kind + '</tr>')
      tbody.append(td)
    }

    $("#show_Content").append(tbody);
  });