//登録用アラート
function registMessage(){
	if(confirm('この内容で登録しますか？')){
		return true;
	}else{
		return false;
	}
}
//削除用アラート
function deleteMessage() {
	if(confirm('削除しますか？')){
		return true;
	}else{
		return false;
	}
}

//完了用アラート
function completeMessage(){
	if(confirm('この内容で登録しますか？')){
		return true;
	}else{
		return false;
	}
}
//中止用アラート
function stopMessage() {
	return confirm('削除しますか？');
}

//必須項目用アラート これをもとにconstを追加したり、loginId部分を変更したりしてください！！
//function checkRequierd(){
//	const nanndemo = document.getElementById(nanndemo).value;
//	
//	if(loginId === ""){
//		alert("必須項目を入力してください");
//		return false;
//	}
//	
//	return true;
//}