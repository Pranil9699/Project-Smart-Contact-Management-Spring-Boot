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

const search = () => {
	console.log("Searching");
	let query = $("#search-input").val();
	if (query == "") {
		console.log("Empty");
		$(".search-result").hide();


	} else {
		$(".search-result").show();
		console.log("Fill")

		let url = `http://localhost:8080/search/${query}`;
		fetch(url)
			.then((response) => {
				return response.json();
			}
			).then((data) => {
				console.log(data);
				console.log(data.length);

				let text = `<div class='list-group'>`;

				data.forEach((contact) => {
					text += `<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`;
				});
				text += `</div>`;
				$(".search-result").html(text);
				$(".search-result").show();

			});



	}
}




// first req. to server to create order

const paymentStart = () => {
	console.log("Done...");

	let amount = $("#payment_field").val();
	console.log(amount)

	if (amount == "" || amount == null) {
		/*	alert("")*/
		swal("Required!", "Amount is Required!!!", "warning");
		return
	}

	$.ajax({
		url: '/user/create_order',
		data: JSON.stringify({ amount: amount, info: 'order_req' }),
		contentType: 'application/json',
		type: 'POST',
		datatype: 'json',
		success: function(res) {
			console.log(res)

			console.log("1111111111")
			let response = JSON.parse(res);
			console.log(response.status);
			if (response.status == "created") {
				//open payment form
				console.log("2222222222")
				let options = {
					key: 'rzp_test_QFvMW8KhGXxtl9',
					amount: response.amount,
					currency: 'INR',
					name: "Smart Contact Manager",
					discription: "Donation",
					image: "https://avatars.githubusercontent.com/u/112823334?s=280&v=4",
					order_id: response.id,
					handler: function(response) {
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						//console.log("Payment SuccessFull!!!");

						updatePayment(response.razorpay_payment_id
							, response.razorpay_order_id, 'Paid');










						//swal("Good Job!", "Payment SuccessFull!!! Congrates!..", "success");
						//alert("Congrates!..");
					},
					prefill: { //We recommend using the prefill parameter to auto-fill customer's contact information, especially their phone number
						name: "", //your customer's name
						email: "",
						contact: ""
					}, notes: {
						address: "Pranil Home Sweet Home"
					},
					theme: {
						color: "#3399cc"
					}
				};

				console.log("22222222223")
				let rzp = new Razorpay(options);
				rzp.on('payment.failed', function(response) {
					console.log(response.error.code);
					console.log(response.error.description);
					console.log(response.error.source);
					console.log(response.error.step);
					console.log(response.error.reason);
					console.log(response.error.metadata.order_id);
					console.log(response.error.metadata.payment_id);
					alert("Oops Payment Failed !!");
					//swal("Failed !", "Oops Payment Failed !!", "error");

				});
				console.log("22222222224")

				rzp.open();



			}
			console.log("1111111111")
		},
		error: function(error) {
			console.log(error)
			swal("Failed !", error, "error");
		}
	});

}





function updatePayment(P_id, o_id, status)
{
	$.ajax({
		url: "/user/update_order",
		data: JSON.stringify({ paymentid: P_id, orderid: o_id, status: status }),
		contentType: "application/json",
		type: "POST",
		datatype: "json",
		 success: function(response) {
      console.log(response.message); // Access the response message
      swal("Good Job!", response.message, "success");
    },
		error: function(error) {
			console.log(error)
			swal("Failed !", "Your Payment is Successful, But we did not get on server, we will conyaycy you as soon as by Razorpay.com !!", "error");
		}
	});
}













