/**
 * 
 */


document.addEventListener("DOMContentLoaded", () => {
	
	loadTab("posts", 0);
	
	document.querySelectorAll(".tab-link").forEach(link => {
		link.addEventListener("click", function(e){
			e.preventDefault();
			const tab = this.dataset.tab;
			
			document.querySelectorAll(".menu-item").forEach(li => li.classList.remove("active"));
			this.parentElement.classList.add("active");
			
			loadTab(tab, 0);
		})
	})
	
});


function loadTab(tab, page=0) {
	fetch(`/member/${tab}?page=${page}`)
		.then(res => res.text())
		.then(html => {
			document.getElementById("tab-content").innerHTML = html;
		})
		.catch(err => {
			console.error("탭 로드 실패: ", err);
			document.getElementById("tab-content").innerHTML = "<p>내용을 불러오지 못했습니다.</p>"
		})
	
}

function deletePosts() {
	
	const checkBoxes = document.querySelectorAll('.board-check:checked');
	
	if(checkBoxes.length === 0){
		alert("삭제할 게시글을 선택해주세요.");
		return;
	}
	
	const postNos = Array.from(checkBoxes).map(cb => {
		return cb.closest('tr').querySelector('.board-number').textContent;
	});
	
	console.log(postNos);
	
	fetch(`/member/deletePosts`, {
		method: 'POST',
		headers: { 
			"Content-Type": "application/json",
			"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
		},
		body: JSON.stringify(postNos)
	})
	.then(res => res.json())
	.then(data => {
		if(data.success){
			alert("게시글 삭제 완료");
			location.reload();
		} else {
			alert("게시글 삭제 실패");
		}
	})
	.catch(err => console.error(err));
}

function deleteComments() {
	
	const checkBoxes = document.querySelectorAll('.board-check:checked');
		
	if(checkBoxes.length === 0){
		alert("삭제할 댓글을 선택해주세요.");
		return;
	}
		
	const commentIds = Array.from(checkBoxes).map(cb => {
		return cb.closest('tr').querySelector('.comment-id').value;
	});
	
	console.log(commentIds);
	
	fetch(`/member/deleteComments`, {
		method: 'POST',
		headers: { 
			"Content-Type": "application/json",
			"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
		},
		body: JSON.stringify(commentIds)
	})
	.then(res => res.json())
	.then(data => {
		if(data.success){
			alert("댓글 삭제 완료");
			location.reload();
		} else {
			alert("댓글 삭제 실패");
		}
	})
	.catch(err => console.error(err));
}

function cancelLikes(){
	const checkBoxes = document.querySelectorAll('.board-check:checked');
	
	if(checkBoxes.length === 0){
		alert("취소할 좋아요를 선택해주세요.");
		return;
	}
	
	const likeyoNos = Array.from(checkBoxes).map(cb => {
		return cb.closest('tr').querySelector('.like-no').value;
	});
	
	console.log(likeyoNos);
	
	fetch(`/member/cancelLikes`, {
		method: 'POST',
		headers: { 
			"Content-Type": "application/json",
			"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
		},
		body: JSON.stringify(likeyoNos)
	})
	.then(res => res.json())
	.then(data => {
		if(data.success){
			alert("좋아요 취소 완료");
			location.reload();
		} else {
			alert("좋아요 취소 실패");
		}
	})
	.catch(err => console.error(err));	
}