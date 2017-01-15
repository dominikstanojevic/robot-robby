<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Robot Robby - Simulation</title>
    <meta charset="utf-8">

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/simulation.js"></script>
    <script type="text/javascript" src="resources/js/GridLoader.js"> </script>
    <script type="text/javascript" src="resources/js/bootstrap-filestyle.min.js"> </script>

    <script type="text/javascript">
        function init(){
            optionSelected();
            setMapCreation();
            sectionA();

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
                processData: false
            });
        }

        function setMapCreation(){
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
    </script>

</head>
<body onload="init()">
    <jsp:include page="about.jsp"/>

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
                    <form id="generateGridForm0" class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-md-3 col-md-offset-1" for="width">Width</label>
                            <div class="col-md-8">
                                <input type="text" name="width" id="width" value="10" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3 col-md-offset-1" for="height">Height</label>
                            <div class="col-md-8">
                                <input type="text" name="height" id="height" value="10" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-3 col-md-offset-1" for="percent">% bottles</label>
                            <div class="col-md-8">
                                <input type="text" name="percentage" id="percent" value="0.5" class="form-control">
                            </div>
                        </div>

                    </form>

                    <div class="row">
                        <div class="col-md-3 col-md-offset-9">
                            <button onclick="generateMap()" class="btn btn-default btn-block"> Generate map </button>
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
                            <button onclick="importMap()" class="btn btn-default btn-block">Import map</button> <br/>
                        </div>
                    </div>
                </div>

                <div id="sectionC" class="row">
                    <div id="sectionC1">
                        <div class="row">
                            <form id="createGridForm" class="form-horizontal">
                                <div class="form-group col-md-6">
                                    <label class="control-label col-md-3 col-md-offset-2" for="widthCreate">Width</label>
                                    <div class="col-md-7">
                                        <input type="text" name="width" id="widthCreate" value="10" class="form-control">
                                    </div>
                                </div>

                                <div class="form-group col-md-6">
                                    <label class="control-label col-md-3 col-md-offset-1" for="heightCreate">Height</label>
                                    <div class="col-md-8">
                                        <input type="text" name="height" id="heightCreate" value="10" class="form-control">
                                    </div>
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
                            <div class="col-md-12">
                                <canvas id="gridCanvas" height="480" width="640"> </canvas>
                            </div>
                        </div>

                        <br/>
                        <div class="row">
                            <div class="col-md-3 col-md-offset-9">
                                <button onclick="createMap()" class="btn btn-default btn-block">Create map</button>
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
                        <button onclick="loadRobot()" class="btn btn-default btn-block">Import robot</button>
                    </div>
                </div>
            </div>
        </div>

        <br/>
        <hr/>
        <br/>

        <div class="row" id = "simulationDiv">
            <form method="post" class="form-horizontal">
                <input type="submit" value="Start simulation" class="btn btn-default">
            </form>

            <canvas id="canvas" height="480" width="640"> </canvas>
            <button id = "btn1" onclick="simulation.previous()" class="btn btn-default"> Previous </button>
            <button id = "btn2" onclick="toggle()" class="btn btn-default"> Play </button>
            <button id = "btn3" onclick="simulation.next()" class="btn btn-default"> Next </button>
            <button id = "btn4" onclick="reset()" class="btn btn-default"> Reset </button>

        </div>
    </div>
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
