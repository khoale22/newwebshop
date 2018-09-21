<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>  
    <title>AngularJS $http Example</title>  
    
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
     <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
      
     
  </head>
  <body ng-app="myApp" class="ng-cloak">
      <div class="generic-container" ng-controller="UserController as ctrl">


          <form ng-submit="ctrl.submit()" name="myForm">
      <%--  <td><span ng-model="ctrl.user.id"></span></td>
                  <td><span ng-bind="ctrl.user.username"></span></td>
                  <td><span ng-bind="ctrl.user.address"></span></td>
                  <td><span ng-bind="ctrl.user.email"></span></td>--%>
                    <%--<input type="text" ng-model="ctrl.user.id" name="uname" />--%>
                    <input type="text" ng-model="ctrl.user.username" name="uname" ng-minlength="3" />
                    <input type="text" ng-model="ctrl.user.address" name="uname" />
                    <input type="text" ng-model="ctrl.user.email" name="uname" />
                    <input type="submit" value="{{!ctrl.user.id? 'Add' : 'Update'}}" ng-disabled="myForm.$invaild"/>
                    <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm" ng-disabled="myForm.$pristine">Reset Form</button>
                    </form>
      
      
       <div class="panel panel-default">
                <!-- Default panel contents -->
              <div class="panel-heading"><span class="lead">List of Users5 </span></div>
              <div class="tablecontainer">
                  <table class="table table-hover">
                      <thead>
                          <tr>
                              <th>ID.</th>
                              <th>Nameww</th>
                              <th>Address232</th>
                              <th>Email1</th>
                              <th width="20%"></th>
                          </tr>
                      </thead>
                      <tbody>
                          <tr ng-repeat="u in ctrl.users">
                              <td><span ng-bind="u.id"></span></td>
                              <td><span ng-bind="u.username"></span></td>
                              <td><span ng-bind="u.address"></span></td>
                              <td><span ng-bind="u.email"></span></td>
                              <td>
                              <button type="button" ng-click="ctrl.edit(u.id)" class="btn btn-success custom-width">Edit</button>  <button type="button" ng-click="ctrl.remove(u.id)" class="btn btn-danger custom-width">Remove</button>
                               </td>
                          </tr>
                      </tbody>
                  </table>
              </div>
          </div>    
      </div>
       <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.js"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
      <script src="<c:url value='/static/js/service/user_service.js' />"></script>
      <script src="<c:url value='/static/js/controller/user_controller.js' />"></script>
  </body>
</html>