<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Robot Robby - Simulation</title>
    <meta charset="utf-8">

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>

    <script type="text/javascript" src="resources/js/simulation.js"></script>
    <script type="text/javascript" src="resources/js/GridLoader.js"> </script>
    <script type="text/javascript" src="resources/js/bootstrap-filestyle.min.js"> </script>

    <script type="text/javascript">
        var canvas = null;
        var simulation = null;

        function init(){
            optionSelected();
            setMapCreation();
            sectionA();

            var link = document.getElementById("navItemSimulation");
            link.setAttribute("class", "active");

            canvas = document.getElementById("canvas");
        }

        function optionSelected() {
            var algorithmSelection = document.getElementById("algorithmSelection");
            var selected = algorithmSelection.options[algorithmSelection.selectedIndex].value;

            document.getElementById("algorithmID").setAttribute("value", selected);
        }

        function loadDefaultRobot(){
            var form = document.forms["robotForm"];
            var formData = new FormData(form);

            $.ajax({
                url: "./loadRobot",
                method: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: roboDefaultSuccess,
                error: roboDefaultErr
            });
        }

        function roboDefaultSuccess(){
            $('#btnLoadDefault')
                .popover({content: "Robot imported successfully!", placement: "top"})
                .popover('show');

            setTimeout(function () {
                $('#btnLoadDefault').popover('hide');
            }, 2000);
        }

        function roboDefaultErr(){
            $('#btnLoadDefault')
                .popover({content: "Unable to import robot!", placement: "top"})
                .popover('show');
            $('.popover')
                .css('background-color', '#d9534f')
                .css('color', 'darkred');

            setTimeout(function () {
                $('#btnLoadDefault').popover('hide');
                $('.popover')
                    .css('background-color', 'white')
                    .css('color', '#333');
            }, 2000);
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
                processData: false,
                success: roboLoadSuccess,
                error: roboLoadErr
            });
        }

        function roboLoadSuccess(){
            $('#btnLoadRobot')
                .popover({content: "Robot imported successfully!", placement: "top"})
                .popover('show');

            setTimeout(function () {
                $('#btnLoadRobot').popover('hide');
            }, 2000);
        }

        function roboLoadErr(){
            $('#btnLoadRobot')
                .popover({content: "Unable to import robot!", placement: "top"})
                .popover('show');
            $('.popover')
                .css('background-color', '#d9534f')
                .css('color', 'darkred');

            setTimeout(function () {
                $('#btnLoadRobot').popover('hide');
                $('.popover')
                    .css('background-color', 'white')
                    .css('color', '#333');
            }, 2000);
        }

        function generateMap(){
            $('#generateGridForm').validator('validate');
            var form = document.forms["generateGridForm"];
            var formData = new FormData(form);

            $.ajax({
                url: "./generateGrid",
                method: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    $('#btnGenerate').popover({content: "Map generated successfully!", placement: "top"}).popover('show');

                    setTimeout(function () {
                        $('#btnGenerate').popover('hide');
                    }, 2000);
                }
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
                processData: false,
                success: function() {
                    $('#btnImport')
                        .popover({content: "Map imported successfully!", placement: "top"})
                        .popover('show');

                    setTimeout(function () {
                        $('#btnImport').popover('hide');
                    }, 2000);
                },
                error: function(){
                    $('#btnImport')
                        .popover({content: "Unable to import map!", placement: "top"})
                        .popover('show');
                    $('.popover')
                        .css('background-color', '#d9534f')
                        .css('color', 'darkred');

                    setTimeout(function () {
                        $('#btnImport').popover('hide');
                        $('.popover')
                            .css('background-color', 'white')
                            .css('color', '#333');
                    }, 2000);
                }
            });
        }

        var gridLoader;
        function createMap() {
            document.getElementById("sectionC1").className = "";
            document.getElementById("sectionC2").className = "row collapse";

            var grid = gridLoader.getGrid();

            var formData = new FormData();
            formData.append("grid", JSON.stringify(grid));

            $.ajax({
                url: "./createGrid",
                method: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    $('#btnCreate').popover({content: "Map generated successfully!", placement: "top"}).popover('show');

                    setTimeout(function () {
                        $('#btnCreate').popover('hide');
                    }, 2000);
                }
            });
        }

        function startSimulation(){
            $('#simulationForm').validator('validate');
            var form = document.forms["simulationForm"];
            var formData = new FormData(form);

            $.ajax({
                url: "./simulation",
                method: "POST",
                dataType: "json",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: loadSimulation,
                error: function(){
                    $('#btnSimulation')
                        .popover({content: "Unable to load simulation! Create a map and robot first!", placement: "top"})
                        .popover('show');
                    $('.popover')
                        .css('background-color', '#d9534f')
                        .css('color', 'darkred');

                    setTimeout(function () {
                        $('#btnSimulation').popover('hide');
                        $('.popover')
                            .css('background-color', 'white')
                            .css('color', '#333');
                    }, 2000);
                }
            });
        }

        function loadSimulation(data){
            if (simulation != undefined && simulation != null) reset();
            var grid = data.gridObject;
            var moves = data.moves;
            var maxMove = data.maxMove;
            var algorithmName = data.algorithmName;

            document.getElementById("algorithmName").innerHTML = algorithmName;
            document.getElementById("simulationDiv").className = "row";
            var display = document.getElementById("moveDisplay");

            simulation = new Simulation(canvas, grid, moves, display, maxMove);
            simulation.draw();

            $('#btnSimulation').popover({content: "Simulation prepared!", placement: "top"}).popover('show');

            setTimeout(function () {
                $('#btnSimulation').popover('hide');
            }, 2000);
        }

        function setMapCreation(){
            $('#createGridForm').validator('validate');
            var form = document.forms["createGridForm"];

            var width = form.elements["width"].value;
            var height = form.elements["height"].value;

            var canvas = document.getElementById("gridCanvas");
            gridLoader = new GridLoader(canvas, width, height);

            document.getElementById("sectionC1").className = "collapse";
            document.getElementById("sectionC2").className = "row";
        }

        function sectionA(){
            document.getElementById("sectionA").className = "row";
            document.getElementById("sectionB").className = "row collapse";
            document.getElementById("sectionC").className = "row collapse";

            document.getElementById("btnA").className = "btn btn-default active";
            document.getElementById("btnB").className = "btn btn-default";
            document.getElementById("btnC").className = "btn btn-default";
        }

        function sectionB(){
            document.getElementById("sectionA").className = "row collapse";
            document.getElementById("sectionB").className = "row";
            document.getElementById("sectionC").className = "row collapse";

            document.getElementById("btnA").className = "btn btn-default";
            document.getElementById("btnB").className = "btn btn-default active";
            document.getElementById("btnC").className = "btn btn-default";
        }

        function sectionC() {
            document.getElementById("sectionA").className = "row collapse";
            document.getElementById("sectionB").className = "row collapse";
            document.getElementById("sectionC").className = "row";

            document.getElementById("sectionC1").className = "";
            document.getElementById("sectionC2").className = "row collapse";

            document.getElementById("btnA").className = "btn btn-default";
            document.getElementById("btnB").className = "btn btn-default";
            document.getElementById("btnC").className = "btn btn-default active";
        }

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

        function reset(){
            isPaused = true;
            document.getElementById("btn1").disabled = false;
            document.getElementById("btn2").innerHTML = "Play";
            document.getElementById("btn3").disabled = false;

            simulation.reset();
        }


    </script>

</head>
<body onload="init()">
<jsp:include page="navbar.jsp"/>

<div class="container-fluid" style="margin: 10px 10px 10px 10px">
    <div class="row">
        <div class="col-md-6">
            <div class="row">
                <div class="btn-group btn-group-justified" role="group">
                    <div class="btn-group" role="group">
                        <button id="btnA" type="button" class="btn btn-default active" onclick="sectionA()">Generate</button>
                    </div>
                    <div class="btn-group" role="group">
                        <button id="btnB" type="button" class="btn btn-default" onclick="sectionB()">Import</button>
                    </div>
                    <div class="btn-group" role="group">
                        <button id="btnC" type="button" class="btn btn-default" onclick="sectionC()">Create</button>
                    </div>
                </div>
            </div>
            <br/>

            <div id="sectionA" class="row">
                <form id="generateGridForm" class="form-horizontal" data-toggle="validator" role="form">
                    <div class="form-group">
                        <label class="control-label col-md-3 col-md-offset-1" for="width">Width</label>
                        <div class="col-md-8">
                            <input type="number" min="3" max="25" required step="1" name="width" id="width" value="10" class="form-control">
                        </div>
                        <div class="help-block with-errors col-md-8 col-md-offset-4"></div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-md-3 col-md-offset-1" for="height">Height</label>
                        <div class="col-md-8">
                            <input type="number" min="3" max="25" required step="1" name="height" id="height" value="10" class="form-control">
                        </div>
                        <div class="help-block with-errors col-md-8 col-md-offset-4"></div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-md-3 col-md-offset-1" for="percent">% bottles</label>
                        <div class="col-md-8">
                            <input type="number" min="0" max="100" required step="0.1" name="percentage" id="percent" value="50" class="form-control">
                        </div>
                        <div class="help-block with-errors col-md-8 col-md-offset-4"></div>
                    </div>

                </form>

                <div class="row">
                    <div class="col-md-3 col-md-offset-9">
                        <button id="btnGenerate" onclick="generateMap()" class="btn btn-default btn-block"> Generate map </button>
                    </div>
                </div>
            </div>

            <div id="sectionB" class="row">
                <div class="row">
                    <form id="importGridForm" class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-md-3" for="gridFile">Load grid</label>
                            <div class="col-md-8">
                                <input id="gridFile" type="file" name="gridFile" class="filestyle"
                                       data-buttonText="Find file" data-buttonName="btn-primary">
                            </div>
                        </div>
                    </form>
                </div>


                <div class="row">
                    <div class="col-md-3 col-md-offset-8">
                        <button id="btnImport" onclick="importMap()" class="btn btn-default btn-block">Import map</button> <br/>
                    </div>
                </div>
            </div>

            <div id="sectionC" class="row">
                <div id="sectionC1">
                    <div class="row">
                        <form id="createGridForm" class="form-horizontal" data-toggle="validator" role="form">
                            <div class="form-group col-md-6">
                                <label class="control-label col-md-3 col-md-offset-2" for="widthCreate">Width</label>
                                <div class="col-md-7">
                                    <input type="number" min="3" max="25" required step="1" name="width" id="widthCreate" value="10" class="form-control">
                                </div>
                                <div class="help-block with-errors col-md-7 col-md-offset-5"></div>
                            </div>

                            <div class="form-group col-md-6">
                                <label class="control-label col-md-3 col-md-offset-1" for="heightCreate">Height</label>
                                <div class="col-md-8">
                                    <input type="number" min="3" max="25" required step="1" name="height" id="heightCreate" value="10" class="form-control">
                                </div>
                                <div class="help-block with-errors col-md-8 col-md-offset-4"></div>
                            </div>
                        </form>
                    </div>

                    <div class="row">
                        <div class="col-md-3 col-md-offset-8">
                            <button onclick="setMapCreation()" class="btn btn-default btn-block">Set</button> <br/>
                        </div>
                    </div>
                </div>

                <div id="sectionC2" class="row collapse">
                    <div class="row">
                        <div class="col-md-11 col-md-offset-1">
                            <canvas id="gridCanvas" height="480" width="640"> </canvas>
                        </div>
                    </div>

                    <br/>
                    <div class="row">
                        <div class="col-md-3 col-md-offset-9">
                            <button id="btnCreate" onclick="createMap()" class="btn btn-default btn-block">Create map</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="col-md-6">
            <div class="row">
                <div class="col-md-6 col-md-offset-1">
                    <select onchange="optionSelected()" id="algorithmSelection" class="selectpicker" data-width="auto">
                        <c:forEach var="algorithm" items="${algorithms.entrySet()}">
                            <option value="${algorithm.key}">${algorithm.value}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col-md-3 col-md-offset-1">
                    <button id="btnLoadDefault" onclick="loadDefaultRobot()" class="btn btn-default btn-block">Default robot</button>
                </div>
            </div>
            <br/>

            <div class="row">
                <form id="robotForm" class="form-horizontal">
                    <input name="algorithmID" id="algorithmID" type="hidden">

                    <div class="form-group">
                        <label class="control-label col-md-3" for="robotFile">Load robot</label>
                        <div class="col-md-8">
                            <input id="robotFile" type="file" name="robotFile" class="filestyle"
                                   data-buttonText="Find file" data-buttonName="btn-primary">
                        </div>
                    </div>
                </form>
            </div>

            <div class="row">
                <div class="col-md-3 col-md-offset-8">
                    <button id="btnLoadRobot" onclick="loadRobot()" class="btn btn-default btn-block">Import robot</button>
                </div>
            </div>
        </div>
    </div>

    <br/>
    <hr/>
    <br/>

    <div class="row">
        <div class=" col-md-4 col-md-offset-1">
            <form id="simulationForm" class="form-horizontal" data-toggle="validator" role="form">
                <div class="form-group">
                    <label class="control-label col-md-5" for="maxMoves">Max moves</label>
                    <div class="col-md-7">
                        <input type="number" min="20" max="1000" required step="1" name="maxMoves" id="maxMoves" value="200" class="form-control">
                    </div>
                    <div class="help-block with-errors col-md-7 col-md-offset-5"></div>
                </div>
            </form>
        </div>

        <div class="col-md-3">
            <button id="btnSimulation" onclick="startSimulation()" class="btn btn-default btn-block">
                Start simulation
            </button>
        </div>
    </div>

    <br/>

    <div class="row collapse" id = "simulationDiv">
        <div class="row">
            <div class="col-md-7 col-md-offset-1">
                <form method="post" class="form-horizontal">
                    <canvas id="canvas" class="img-responsive" height="768" width="1024"> </canvas>
                </form>
            </div>

            <div class="col-md-3">
                <div class="row">
                    <div class="col-md-12">
                        <h4 class="text-center" id="algorithmName">/</h4>
                    </div>
                    <div class="col-md-12">
                        <h4 class="text-center" id="moveDisplay">/</h4>
                    </div>
                </div>

                <br/>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <button id = "btn1" onclick="simulation.previous()" class="btn btn-default btn-block">
                            Previous
                        </button>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <button id = "btn2" onclick="toggle()" class="btn btn-default btn-block">
                            Play
                        </button>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <button id = "btn3" onclick="simulation.next()" class="btn btn-default btn-block">
                            Next
                        </button>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        <button id = "btn4" onclick="reset()" class="btn btn-default btn-block btn-block">
                            Reset
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
