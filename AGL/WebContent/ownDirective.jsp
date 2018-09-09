<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script
	src="//ajax.googleapis.com/ajax/libs/angularjs/1.3.2/angular.min.js"></script>

<script language="javascript">
angular.module('myapp' ,[]).controller('MyController' , function($scope) {
	//
}).directive('myFormregister' , function() {
var html = '<table>';
     	html += '<tr>'
     	      html += '<td> Username: </td>'
     	      html += '<td><input type="text"/></td>'
     	html += '</tr>'
     		html += '<tr>'
     	      html += '<td> Password </td>',
     	      html += '<td><input type="password"/></td>'
     	html += '</tr>'
     	html += '<tr>'
              html += '<td></td>'
              html += '<td><input type="Button" value="Login"/></td>'
        html += '</tr>'
    html += '</table>'
    
    return{
    	template : html 
    };
});
 
</script>
</head>
<body ng-app="myapp">
	<div ng-controller="MyController">
		<div my-formregister></div>
	</div>
</body>
</html>