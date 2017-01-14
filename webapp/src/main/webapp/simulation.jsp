<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Simulation demo</title>
    <meta charset="utf-8">

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/simulation.js"></script>
    <script type="text/javascript" src="resources/js/GridLoader.js"> </script>

    <script type="text/javascript">
        function init(){
            optionSelected();
            setMapCreation();

            var link = document.getElementById("navItemSimulation");
            link.setAttribute("class", "active");
        }

        function optionSelected() {
            var algorithmSelection = document.getElementById("algorithmSelection");
            var selected = algorithmSelection.options[algorithmSelection.selectedIndex].value;

            document.getElementById("algorithmID").setAttribute("value", selected);
        }

        function loadRobot(){
            var form = document.forms["robotForm"];
            var formData = new FormData(form);

            $.ajax({
                url: "./importRobot",
                method: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false
            });
        }

        function generateMap(){
            var form = document.forms["generateGridForm"];
            var formData = new FormData(form);

            $.ajax({
                url: "./generateGrid",
                method: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false
            });
        }

        function importMap(){
            var form = document.forms["importGridForm"];
            var formData = new FormData(form);

            $.ajax({
                url: "./importGrid",
                method: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false
            });
        }

        var gridLoader;
        function createMap() {
            var grid = gridLoader.getGrid();

            var formData = new FormData();
            formData.append("grid", JSON.stringify(grid));

            $.ajax({
                url: "./createGrid",
                method: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false
            });
        }

        function setMapCreation(){
            var form = document.forms["createGridForm"];

            var width = form.elements["width"].value;
            var height = form.elements["height"].value;

            var canvas = document.getElementById("gridCanvas");
            gridLoader = new GridLoader(canvas, width, height);
        }
    </script>

</head>
<body onload="init()">
    <jsp:include page="about.jsp"/>

    <p><select onchange="optionSelected()" id="algorithmSelection">
        <c:forEach var="algorithm" items="${algorithms.entrySet()}">
            <option value="${algorithm.key}">${algorithm.value}</option>
        </c:forEach>
    </select></p>

    <p><form id="robotForm">
        <input name="algorithmID" id="algorithmID" type="hidden">

        Load Robot: <input type="file" name="robotFile">
    </form>

    <button onclick="loadRobot()">Import robot</button>

    <br/>
    <hr/>
    <br/>

    <form id="generateGridForm">
        <input type="text" name="width" value="10">
        <input type="text" name="height" value="10">
        <input type="text" name="percentage" value="0.5">
    </form>
    <button onclick="generateMap()" > Generate map </button> <br/>

    <form id="importGridForm">
        <input type="file" name="gridFile">
    </form>
    <button onclick="importMap()">Import map</button> <br/>


    <div id="createMapArea">
        <form id="createGridForm">
            Width: <input type="text" name="width" value="10">
            Height: <input type="text" name="height" value="10">
        </form>
        <button onclick="setMapCreation()">Set</button> <br/>

        <canvas id="gridCanvas" height="480" width="640"> </canvas>
        <button onclick="createMap()">Create map</button>
    </div>

    <br/>
    <hr/>
    <br/>
    <form method="post">
        <input type="submit" value="Start simulation">
    </form>

    <canvas id="canvas" height="480" width="640"> </canvas>
    <button id = "btn1" onclick="simulation.previous()" > Previous </button>
    <button id = "btn2" onclick="toggle()" > Play </button>
    <button id = "btn3" onclick="simulation.next()" > Next </button>
    <button id = "btn4" onclick="reset()" > Reset </button>

    <script type="text/javascript">
        var grid = ${grid};
        var moves = ${moves};

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
        };

        var reset = function(){
            isPaused = true;
            document.getElementById("btn1").disabled = false;
            document.getElementById("btn2").innerHTML = "Play";
            document.getElementById("btn3").disabled = false;

            simulation.reset();
        };
    </script>
</body>
</html>
