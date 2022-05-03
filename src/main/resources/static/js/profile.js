$(function(){
	$(".follow-btn").click(follow);
});
//
// function follow() {
// 	var CONTEXT_PATH="/community";
// 	if($(btn).hasClass("btn-info")) {
// 		alert("btn-info");
// 		$.ajax({
// 			type: 'POST',
// 			url: CONTEXT_PATH + "/follow",
// 			data: {"entityType": 3, "entityId":$(btn).prev().val()},
// 			success: function (data) {
// 				data = $.parseJSON(data);
// 				if (data.code == 0) {
// 					window.location.reload();
// 				} else {
// 					alert(data.msg);
// 				}
// 			},
// 		});
// 	}else{
// 		$.ajax({
// 			type: 'POST',
// 			url:CONTEXT_PATH + "/unfollow",
// 			data: {"entityType":3,"entityId":$(btn).prev().val()},
// 			success:function (data){
// 				data = $.parseJSON(data);
// 				if(data.code == 0) {
// 					window.location.reload();
// 				} else {
// 					alert(data.msg);
// 				}
// 			}
// 		});
// 	}
// }

function follow() {
	// var CONTEXT_PATH="/community";
	var btn = this;
	if($(btn).hasClass("btn-info")) {
		// 关注TA
		$.post(
			CONTEXT_PATH + "/follow",
			{"entityType":3,"entityId":$(btn).prev().val()},
			function(data) {
				data = $.parseJSON(data);
				if(data.code == 0) {
					window.location.reload();
				} else {
					alert(data.msg);
				}
			}
		);
		// $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
		$.post(
			CONTEXT_PATH + "/unfollow",
			{"entityType":3,"entityId":$(btn).prev().val()},
			function(data) {
				data = $.parseJSON(data);
				if(data.code == 0) {
					window.location.reload();
				} else {
					alert(data.msg);
				}
			}
		);
		//$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
	}
}