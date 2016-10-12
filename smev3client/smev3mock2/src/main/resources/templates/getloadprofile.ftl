<!DOCTYPE html>
<html>
<head>
    <style>
        table, th, td {
            border: 2px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 15px;
        }
    </style>
</head>
<body>
<h1>Current load profile:</h1>
<table style="width:50%">
      <tr>
        <th>ResponseType</th>
        <th>Weight</th>
      </tr>
    <tr>
        <td>withoutAttachmentWeight</td>
        <td>${withoutAttachmentWeight}</td>
    </tr>
    <tr>
        <td>withEmbeddedWeigth</td>
        <td>${withEmbeddedWeigth}</td>
    </tr>
    <tr>
        <td>withFptWeight</td>
        <td>${withFptWeight}</td>
    </tr>
</table>
</body>
</html>
