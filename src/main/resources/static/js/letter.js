$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();
	// var CONTEXT_PATH="/community";

	$.ajax({
		type:'POST',
		url:CONTEXT_PATH + "/letter/send",
		data:{"toName":toName,"content":content},
		success:function (data){
			data=$.parseJSON(data);
			if(data.code == 0) {
				$("#hintBody").text("发送成功!");
			} else {
				$("#hintBody").text(data.msg);
			}
			//显示提示框
			$("#hintModal").modal("show");
			//2s自动隐藏
			setTimeout(function(){
				$("#hintModal").modal("hide");
				if(data.code==0){
					window.location.reload();
				}
			}, 2000);
		},
		error:function (e) {
			console.log(e);
		}
	});


}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}