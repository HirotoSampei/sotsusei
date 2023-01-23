function selectfunc(comment_date,commentuser_id) {
    
    const user_id = document.getElementById("user_id_for_comment").value;
    if(commentuser_id == user_id){
        document.getElementById(comment_date).classList.remove('hide');

    }
}

function deletefunc(comment_date) {
    const comp_id = document.getElementById("comp_id_for_comment").value;
    $.ajax({
        url: '/comp/deletepubliccomment',
        type: "POST",
        data: {
        comp_id: comp_id,
        comment_date: comment_date,
        _csrf: $("*[name=_csrf]").val()
        },
        dataType : "text",
    })
    .done(function(data){
        const commentDataList = JSON.parse(data);
        let i = 0;
        $("#commentBox").replaceWith('<div class="chat" id="commentBox"> </div>');
        for(i = 0; i < commentDataList.length; i++){
            let divTag = $("<div />");
            $("#commentBox").append($('<label for="c2" class="comment-log" id="'+decodeURI(commentDataList[i].user_id)+'" onclick="selectfunc('+"'"+commentDataList[i].comment_date+"'"+","+"'"+commentDataList[i].user_id+"'"+')"></label>').text(decodeURI(commentDataList[i].user_name) + ":" + decodeURI(commentDataList[i].comment)));
            $('#commentBox').append($('<input type="checkbox" id="c2">'));
            $('#commentBox').append($('<button class="delete hide" id="'+decodeURI(commentDataList[i].comment_date)+'" onclick="deletefunc('+"'"+commentDataList[i].comment_date+"'"+')">削除</button>'));
            $('#commentBox').append(divTag);
        }
        $("#comment").val(""); 
    })
    .fail(function(XMLHttpRequest, status, e){
        alert(XMLHttpRequest,status,e);
    });   
}