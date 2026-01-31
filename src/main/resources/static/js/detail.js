let originPosition = "";

function showEdit() {
	originPosition = document.getElementById("position-view").innerText.trim();
	
	document.getElementById("title-view").style.display = "none";
	document.getElementById("title-edit").style.display = "block";
	
	document.getElementById("recruitment-view").style.display = "none";
	document.getElementById("recruitment-edit").style.display = "inline";
	
	document.getElementById("position-view").style.display = "none";
	document.getElementById("position-edit").style.display = "inline";
	
	document.getElementById("cont-view").style.display = "none";
	document.getElementById("cont-edit").style.display = "block";
	
	document.querySelectorAll(".button-area button").forEach(btn => btn.classList.remove("activeBt"));
	document.querySelector(".cancelBt").classList.add("activeBt");
	document.querySelector(".saveBt").classList.add("activeBt");
	
	
}

function cancelEdit(){
		document.getElementById("title-view").style.display = "block";
		document.getElementById("title-edit").style.display = "none";
		
		document.getElementById("recruitment-view").style.display = "inline";
		document.getElementById("recruitment-edit").style.display = "none";
		
		document.getElementById("position-view").style.display = "inline";
		document.getElementById("position-edit").style.display = "none";
		
		const selected = originPosition.split(",").map(v => v.trim());
		
		document.querySelectorAll('input[name="position-edit"]').forEach(cb => {
			cb.checked = selected.includes(cb.value);
		});
		
		document.getElementById("cont-view").style.display = "block";
		document.getElementById("cont-edit").style.display = "none";
		
		document.querySelectorAll(".button-area button").forEach(btn => btn.classList.remove("activeBt"));
		document.querySelector(".editBt").classList.add("activeBt");
		document.querySelector(".deleteBt").classList.add("activeBt");
		document.querySelector(".goList").classList.add("activeBt");
}

function submitEdit(){
		const postNo = document.getElementById("postNo").value;
		const title = document.getElementById("title-edit").value;
		const recruitment = document.getElementById("recruitment-edit").value;
		const text = document.getElementById("cont-edit").value;
		const positions = Array.from( 
			document.querySelectorAll('input[name="position-edit"]:checked')).map(cb => cb.value);
		const positionStr = positions.join(",");
		
		fetch(`/member/editPost/${postNo}`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
			},
			body: JSON.stringify({
				postTitle: title,
				recruitment: recruitment,
				postText: text, 
				position: positionStr
			})
		})
		.then(response => response.json())
		.then(data => {
			originPosition = data.position;
			
			document.getElementById("title-view").innerText = data.postTitle;
			document.getElementById("recruitment-view").innerText = data.recruitment;
			document.getElementById("position-view").innerText = data.position;
			document.getElementById("cont-view").innerText = data.postText;
			
			document.getElementById("title-view").style.display = "block";
			document.getElementById("title-edit").style.display = "none";
					
			document.getElementById("recruitment-view").style.display = "inline";
			document.getElementById("recruitment-edit").style.display = "none";
			
			document.getElementById("position-view").style.display = "inline";
			document.getElementById("position-edit").style.display = "none";
					
			document.getElementById("cont-view").style.display = "block";
			document.getElementById("cont-edit").style.display = "none";
			
			document.querySelectorAll(".button-area button").forEach(btn => btn.classList.remove("activeBt"));
			document.querySelector(".editBt").classList.add("activeBt");
			document.querySelector(".deleteBt").classList.add("activeBt");
			document.querySelector(".goList").classList.add("activeBt");
		})
		.catch(err => {
			alert(err.message);
		})
}

function commentEdit(button){
	const commentItem = button.closest('.comment-item');
	
	const commentCont = commentItem.querySelector('.comment-cont');
	const commentEdit = commentItem.querySelector('.comment-edit');
	const saveBt = commentItem.querySelector('.commentSaveEdit');
	
	commentCont.style.display = 'none';
	commentEdit.style.display = 'block';
	
	document.querySelectorAll(".comment-check button").forEach(btn => btn.classList.remove("activeCBt"));
	saveBt.classList.add("activeCBt")
	
}

function commentDelete(button){
	const commentItem = button.closest('.comment-item');
	const commentId = commentItem.querySelector('.comment-id').value;
	
	fetch(`/member/commentDelete/` + commentId , {
		method: "DELETE",
		headers: {
			"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
		}
	})
	.then(res => {
		if (res.ok) {
			commentItem.remove();
		} else if (res.status === 403) {
			alert("권한이 없습니다. 403 Forbidden");
		} else {
			alert("삭제 불가");
		}
	})
	.catch(err => {
		console.error(err);
		alert("오류 발생");
	});
}

function commentSaveEdit(button){
	const commentItem = button.closest('.comment-item');
	const commentId = commentItem.querySelector('.comment-id').value;
	const commentText = commentItem.querySelector('.comment-edit').value;
	
	const editBt = commentItem.querySelector('.commentEditBt');
	const deleteBt = commentItem.querySelector('.commentDeleteBt');
	
	fetch(`/member/editComment/` + commentId, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
		},
			body: JSON.stringify({
				commentText: commentText
			})
		})
		.then(response => response.json())
		.then(data => {
			commentItem.querySelector('.comment-cont').innerText = data.commentText;
				
			commentItem.querySelector('.comment-cont').style.display = "block";
			commentItem.querySelector('.comment-edit').style.display = "none";

				
			document.querySelectorAll(".comment-check button").forEach(btn => btn.classList.remove("activeCBt"));
			editBt.classList.add("activeCBt")
			deleteBt.classList.add("activeCBt")
		})
		.catch(err => {
			alert(err.message);
		})
	
}

window.onload = function(){
	const likeBtn = document.getElementById("likeBt");
	    if (!likeBtn) return;
		
	likeBtn.addEventListener("click", function(){
		const postNo = document.getElementById("postNo").value;
		
		fetch(`/member/likeToggle`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
			},
			body: JSON.stringify({
				postNo: postNo
			})
		})
		.then(res => res.text())
		.then(status => {
			if(status === "Y"){
				this.textContent = "북마크 취소";
			} else {
				this.textContent = "북마크";
			}
		})
		.catch(err => {
			alert(err.message);
		})
		
		
	});
};