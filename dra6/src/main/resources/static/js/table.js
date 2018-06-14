$('tr').on('click',function(){
    var id = $(this).closest('tr').attr('id');
    console.log(id);
});