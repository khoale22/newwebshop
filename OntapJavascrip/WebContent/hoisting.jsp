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
	

</body>
</html>