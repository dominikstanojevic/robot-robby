<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Grid chooser</title>
</head>
<body>
    <canvas id="canvas" width="640" height="480"></canvas>

    <script src="resources/js/GridLoader.js" type="text/javascript"> </script>
    <script type="text/javascript">
        var canvas = document.getElementById("canvas");

        var gl = new GridLoader(canvas, 4, 4);
    </script>
</body>
</html>
