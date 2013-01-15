<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>


<html>
    <head>
        <title>Upload Test</title>
    </head>
    <body>
        <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
            <table>
                <tr>
                    <td>name</td>
                    <td><input type="text" name="name" /></td>
                </tr>
                <tr>
                    <td>email</td>
                    <td><input type="text" name="email" /></td>
                </tr>
                <tr>
                    <td>mobile</td>
                    <td><input type="text" name="mobile" /></td>
                </tr>
                <tr>
                    <td>address</td>
                    <td><textarea name="address"></textarea></td>
                </tr>
                <tr>
                    <td>file</td>
                    <td><input type="file" name="myFile"></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"></td>
                    <td></td>
                </tr>
            </table>
        </form>
    </body>
</html>
