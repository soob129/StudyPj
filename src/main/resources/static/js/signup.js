/**
 * 
 */

document.addEventListener('DOMContentLoaded', () => {
	function checkNickname(){
		const nickname = document.getElementById("userNickname").value;
			
		if(!nickname){
			alert("닉네임을 입력해주세요.");
			return;
		}
			
		fetch(`/user/checkNickname?nickname=${encodeURIComponent(nickname)}&t=${new Date().getTime()}`)
			.then(response => response.text())
			.then(data => {
				const resultArea = document.getElementById("nicknameResult");
				resultArea.style.display = "block";
				if (data === "OK") {
					resultArea.textContent = "사용 가능한 닉네임";
					resultArea.style.color = "green";
				} else {
					resultArea.textContent = "이미 사용 중인 닉네임";
					resultArea.style.color = "red";	
				}
			})
			.catch(error => {
				console.error("중복 확인 실패: ", error);
			});
		}	
	const btn = document.getElementById("checkNickname");
	if(btn){
		btn.addEventListener("click", checkNickname);
	}
	
});
