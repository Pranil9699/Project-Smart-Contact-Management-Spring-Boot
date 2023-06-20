/**
 * 
 */
//alert("hello Pranil")
const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {

		// true
		// we are closing the sideBar
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
		$("#crossBTNMenu").css("display", "block");
		$("#crossBTNMenu").css("margin-top", "50px");
		$("#crossBTNMenu").css("margin-left", "30px");
		
	} else {
		//false 
		// we are opening the sideBar
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
		$("#crossBTNMenu").css("display", "none");
	}
}

const search=()=>{
	console.log("Searching");
	let query = $("#search-input").val();
	if(query==""){
		console.log("Empty");
		$(".search-result").hide();


	}else{
		$(".search-result").show();
		console.log("Fill")
		
		let url = `http://localhost:8080/search/${query}`;
		fetch(url)
		.then((response) => {
			return response.json();
        }
		).then((data)=>{
			console.log(data);
			console.log(data.length);
			
			let text=`<div class='list-group'>`;
			
			data.forEach((contact)=>{
				text+=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`;
			});
			text+=`</div>`;
			$(".search-result").html(text);
			$(".search-result").show();
			
		});
		
		
		
	}
}