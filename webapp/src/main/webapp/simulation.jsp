<html>
<head>
    <title>Simulation demo</title>
    <meta charset="utf-8">

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <script type="text/javascript" src="resources/js/simulation.js"></script>
</head>
<body>
    <jsp:include page="about.jsp"/>

    <canvas id="canvas" height="480" width="640"> </canvas>
    <button id = "btn1" onclick="simulation.previous()" > Previous </button>
    <button id = "btn2" onclick="toggle()" > Play </button>
    <button id = "btn3" onclick="simulation.next()" > Next </button>

    <script type="text/javascript">
        var grid = {"grid":[["BOTTLE","EMPTY","BOTTLE","EMPTY"],
            ["EMPTY","EMPTY","BOTTLE","EMPTY"],
            ["EMPTY","BOTTLE","EMPTY","EMPTY"],
            ["EMPTY","EMPTY","BOTTLE","EMPTY"]],
            "width":4,"height":4,"startX":0,"startY":0,"numberOfBottles":5};
        var moves = ["DOWN","COLLECT","DOWN","LEFT","COLLECT","COLLECT","SKIP_TURN","LEFT","UP","DOWN",
            "SKIP_TURN","SKIP_TURN","DOWN","DOWN","DOWN","RIGHT","DOWN","RIGHT","COLLECT","UP",
            "UP", "COLLECT", "LEFT", "DOWN", "COLLECT", "COLLECT", "UP", "LEFT", "UP", "COLLECT",
            "RIGHT", "RIGHT", "COLLECT", "DOWN"];

        var canvas = document.getElementById("canvas");
        var simulation = new Simulation(canvas, grid, moves);

        simulation.draw();

        var isPaused = true;
        var toggle = function(){
            if (isPaused){
                simulation.play();
                isPaused = false;
                document.getElementById("btn1").disabled = true;
                document.getElementById("btn2").innerHTML = "Pause";
                document.getElementById("btn3").disabled = true;
            } else {
                simulation.pause();
                isPaused = true;
                document.getElementById("btn1").disabled = false;
                document.getElementById("btn2").innerHTML = "Play";
                document.getElementById("btn3").disabled = false;
            }
        }
    </script>
</body>
</html>
