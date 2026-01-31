/**
 * 
 */
let currentTab = "dashboard";

document.addEventListener("DOMContentLoaded", () => {
	
	loadTab("dashboard");
	
	document.querySelectorAll(".nav-item").forEach(link => {
		link.addEventListener("click", function(e){
			e.preventDefault();
			const tab = this.dataset.tab;
			
			currentTab = tab;
			
			document.querySelectorAll(".nav-item").forEach(li => li.classList.remove("active"));
			this.classList.add("active");
			
			loadTab(tab);
		})
	})
	
});

document.addEventListener("click", function(e){
	
	if(currentTab === "users"){
		const detailBt = e.target.closest(".user-detail");
		const deleteBt = e.target.closest(".user-delete");
		const restoreBt = e.target.closest(".user-restore");
		
		//상세가 눌리면
		if(detailBt) {
			const memberNo = detailBt.parentElement.dataset.id;
			console.log("상세보기: ", memberNo);
				
			loadMemberDetail(memberNo);
			return;
		}
			
		if(deleteBt){
			const memberNo = deleteBt.parentElement.dataset.id;
				
			if(!confirm("정말 이 유저를 삭제하시겠습니까?")) {
				return;
			}	
			console.log("삭제: ", memberNo);
				
			deleteMember(memberNo);
			return;
		}
			
		if(restoreBt){
			const memberNo = restoreBt.parentElement.dataset.id;
			console.log("복구: ", memberNo);
				
			restoreMember(memberNo);
			return;
		}
		
		if(e.target.matches("#backToUsers")){
			loadTab("users");
		}
	}
	
	if(currentTab === "posts"){
		const detailBt = e.target.closest(".post-detail");
		const deleteBt = e.target.closest(".post-delete");
		const restoreBt = e.target.closest(".post-restore");
		
		if(detailBt) {
			const postNo = detailBt.parentElement.dataset.id;
			console.log("상세보기: ", postNo);
						
			loadPostDetail(postNo);
			return;
		}
		
		if(deleteBt){
			const postNo = deleteBt.parentElement.dataset.id;
						
			if(!confirm("정말 이 게시글을 삭제하시겠습니까?")) {
				return;
			}	
			console.log("삭제: ", postNo);
						
			deletePost(postNo);
			return;
		}
		
		if(restoreBt){
			const postNo = restoreBt.parentElement.dataset.id;
			console.log("복구: ", postNo);
						
			restorePost(postNo);
			return;
		}
		
	}
	
	
});


function loadTab(tab) {
	fetch(`/admin/${tab}`)
		.then(res => res.text())
		.then(html => {
			document.getElementById("tab-content").innerHTML = html;
			
			const monthlyPostsDiv = document.getElementById('monthlyPostsData');
				if (monthlyPostsDiv) {
			    	const monthlyPosts = JSON.parse(monthlyPostsDiv.getAttribute('data-json'));
			        drawPostChart(monthlyPosts);
			    }
		})
		.catch(err => {
			console.error("탭 로드 실패: ", err);
			document.getElementById("tab-content").innerHTML = "<p>내용을 불러오지 못했습니다.</p>"
		})
	
};

function drawPostChart(monthlyPosts){
	const labels = [];
	const data = [];
	
	for(let i = 1; i <= 12; i++){
		labels.push(i + '월');
		data.push(monthlyPosts[i] || 0);
	}
	
	const ctx = document.getElementById('postChart').getContext('2d');
	new Chart(ctx, {
		type: 'bar',
		data: {
			labels: labels,
			datasets: [{
				label: '글 등록 수',
				data: data,
				backgroundColor: 'rgba(54, 162, 235, 0.5)',
				borderColor: 'rgba(54, 162, 235, 1)',
				borderWidth: 1
			}]
		},
		options: {
			scales: { y: { beginAtZero: true, precision: 0 } }
		}
	});
};

function loadMemberPage(tab, page){

	fetch(`/admin/${tab}?page=${page}`)
		.then(res => res.text())
		.then(html => {
			document.getElementById("tab-content").innerHTML = html;
		})
		.catch(err => {
			console.error("탭 로드 실패: ", err);
			document.getElementById("tab-content").innerHTML = "<p>내용을 불러오지 못했습니다.</p>"
		});
		
}

function loadMemberDetail(memberNo){
	fetch(`/admin/userDetail?memberNo=${memberNo}`)
		.then(res => res.text())
		.then(html => {
			document.getElementById("tab-content").innerHTML = html;
		})
		.catch(err => {
			console.error("상세조회 실패", err);
		});
}

function deleteMember(memberNo){
	fetch(`/admin/deleteMember`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
		},
		body: JSON.stringify({ memberNo : memberNo })
	})
	.then(res => {
		if(!res.ok) throw new Error("삭제 실패!");
		return res.json();
	})
	.then(data => {
		if(data.success){
			alert("삭제 성공");
			loadTab('users');
		} else {
			alert("삭제 실패");
		}
		
	})
	.catch(err => console.error(err));
}

function restoreMember(memberNo){
	fetch(`/admin/restoreMember`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
			},
			body: JSON.stringify({ memberNo : memberNo })
		})
		.then(res => {
			if(!res.ok) throw new Error("복구 실패!");
			return res.json();
		})
		.then(data => {
			if(data.success){
				alert("복구 성공");
				loadTab('users');
			} else {
				alert("복구 실패");
			}
			
		})
		.catch(err => console.error(err));
}

function loadPostDetail(postNo){
	fetch(`/admin/postDetail?postNo=${postNo}`)
		.then(res => res.text())
		.then(html => {
			document.getElementById("tab-content").innerHTML = html;
		})
		.catch(err => {
			console.error("상세조회 실패", err);
		});	
}

function deletePost(postNo){
	fetch(`/admin/deletePost`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
		},
		body: JSON.stringify({ postNo : postNo })
	})
	.then(res => {
		if(!res.ok) throw new Error("삭제 실패!");
		return res.json();
	})
	.then(data => {
		if(data.success){
			alert("삭제 성공");
			loadTab('posts');
		} else {
			alert("삭제 실패");
		}
		
	})
	.catch(err => console.error(err));	
}

function restorePost(postNo){
	fetch(`/admin/restorePost`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				"X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value
			},
			body: JSON.stringify({ postNo : postNo })
		})
		.then(res => {
			if(!res.ok) throw new Error("복구 실패!");
			return res.json();
		})
		.then(data => {
			if(data.success){
				alert("복구 성공");
				loadTab('posts');
			} else {
				alert("복구 실패");
			}
			
		})
		.catch(err => console.error(err));
}