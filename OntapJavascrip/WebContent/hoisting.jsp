<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	// bai1
	<p id="demo"></p>

	<script>
		var x = 5; // Initialize x

		elem = document.getElementById("demo"); // Find an element 
		elem.innerHTML = "x is " + x + " and y is " + y; // Display x and y

		var y = 7; // Initialize y
	</script>


	<!-- ///bai 2
	<p id="demo"></p>

	<script>
		var x = 10;
		// Here x is 10
		{
			var x = 2;
			// Here x is 2
		}
		// Here x is 2
		document.getElementById("demo").innerHTML = x; /// ket qua bang 2
	</script>
	/// bai3
	<p id="demo"></p>

	<script>
		var x = 10;
		// Here x is 10
		{
			let x = 2;
			// Here x is 2 
		}
		// Here x is 10
		document.getElementById("demo").innerHTML = x;  // ket qya bang 10 -->
	<!-- //</script>   -->
	///bai 3 validation
	<input id="id1" type="number" min="100" max="300" required>
	<button onclick="myFunction()">OK</button>
	<p id="demo3"></p>

	<script>
		function myFunction() {
			var inpObj = document.getElementById("id1");
			if (!inpObj.checkValidity()) {
				document.getElementById("demo3").innerHTML = inpObj.validationMessage;
			} else {
				document.getElementById("demo3").innerHTML = "Input OK";
			}
		}
	</script>
	/// bai 5 arguments
	<p id="demo5"></p>

	<script>
		function sumAll() {
			var i;
			var sum = 0;
			for (i = 0; i < arguments.length; i++) {
				sum += arguments[i];

			}
			return sum;
		}
		document.getElementById("demo5").innerHTML = sumAll(1, 123);
	</script>

	// bai 6

	<p id="demo6"></p>

	<script>
		function myFunction(arg1, arg2) {
			this.firstName = arg1;
			this.lastName = arg2;
		}

		var x = new myFunction("John", "Doe")
		document.getElementById("demo6").innerHTML = x.firstName;
	</script>

	/// bai 7
	<p id="demo7"></p>

	<script>
		var person = {
			fullName : function() {
				return this.firstName + " " + this.lastName;
			}
		}
		var person1 = {
			firstName : "John",
			lastName : "Doe",
		}
		var person2 = {
			firstName : s"Mary",
			lastName : "Doe",
		}
		var x = person.fullName.call(person1);
		document.getElementById("demo7").innerHTML = x;
	</script>

	}
</body>
</html>