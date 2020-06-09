function callApi(method,action,data){
    var res='';
    $.ajax({
      type:method,
      url:'/api'+action,
      data:data,
      async:false,
      success: function(result){
        res = result;
      }
    })
    return res;
}