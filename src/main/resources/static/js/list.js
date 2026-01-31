window.onload = function(){
	const searchBt = document.querySelector('#searchBt');
	const keywordInput = document.querySelector('#searchWord');
	const listArea = document.querySelector("#list-cont");
	
	searchBt.addEventListener("click", function(){
		const keyword = keywordInput.value.trim();
		loadList(keyword, 0);
	})
	
	document.addEventListener("click", function (e){
		if(e.target.matches(".pagination a")) {
			e.preventDefault();
			const pageUrl = new URL(e.target.href);
			const page = pageUrl.searchParams.get("page") || 0;
			const keyword = keywordInput.value.trim();
			loadList(keyword, page);
		}
	});
	
	function loadList(keyword, page){
		const url = `/user/showList/fragment?page=${page}` + (keyword ? `&search=${encodeURIComponent(keyword)}` : "");
		
		fetch(url)
			.then((response) => {
				if(!response.ok) throw new Error("서버 통신 오류");
				return response.text();
			})
			.then((html) => {
				const parser = new DOMParser();
				const doc = parser.parseFromString(html, "text/html");
				
				const newList = doc.querySelector("#list-cont");
				const newPage = doc.querySelector(".pagination");
				
				listArea.innerHTML = newList.innerHTML;
				document.querySelector(".pagination").innerHTML = newPage.innerHTML;
			})
			.catch((error) => {
				console.error("목록 불러오기 실패: ", error);
			});
	}
}