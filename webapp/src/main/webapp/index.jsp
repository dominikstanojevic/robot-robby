<html>
<head>
    <title>Index</title>
    <meta charset="utf-8">

    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>

    <script type="text/javascript" src="resources/js/graph.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
</head>

<script type="text/javascript">

    var graph;
    var eventSource;

    function init() {
        optionSelected();

        $.ajax({
            url: "simulatorConfig",
            method: "GET",
            dataType: "json",
            success: refreshSimulatorConfig
        });

        graph = new Graph(
            document.getElementById("plotCanvas"),
            { name: "Iterations", min: 0, max: 1000 },
            { name: "Fitness", min: -0.5, max: 1.0 },
            [
                { color: "#ff0000" },
                { color: "#008000" }
            ]
        );

        var link = document.getElementById("navItemIndex");
        link.setAttribute("class", "active");
    }

    var refreshSimulatorConfig = function (data) {
        var config = data;
        var form = document.getElementById("simulatorConfigForm");

        form["maxMoves"].value = config.maxMoves;
        form["gridHeight"].value = config.gridHeight;
        form["gridWidth"].value = config.gridWidth;
        form["numberOfGrids"].value = config.numberOfGrids;
        form["numberOfBottles"].value = config.numberOfBottles;
        form["mapRegenFrequency"].value = config.mapRegenFrequency;
        form["variableBottles"].checked = config.variableBottles;
    };

    function optionSelected() {
        var algorithmSelection = document.getElementById("algorithmSelection");
        var selected = algorithmSelection.options[algorithmSelection.selectedIndex].value;

        var inputs = document.getElementById("trainingFormInputs");
        inputs.innerHTML = "";

        document.getElementById("algorithmID").setAttribute("value", selected);

        $.ajax(
            {
                url: "parameters",
                dataType: "json",
                data: {
                    algorithmID: selected
                },
                success: function (data) {
                    var parameters = data;

                    for (var i = 0; i < parameters.length; i++) {
                        var parameter = parameters[i];

                        var div = document.createElement("div");
                        div.setAttribute("class", "form-group");

                        var label = document.createElement("label");
                        var input = document.createElement("input");

                        var inputDiv = document.createElement("div");
                        inputDiv.appendChild(input);

                        label.setAttribute("class", "control-label col-sm-6");
                        input.setAttribute("class", "form-control");

                        inputDiv.setAttribute("class", "col-sm-6");

                        input.setAttribute("type", "number");
                        input.setAttribute("name", parameter.name);
                        input.setAttribute("value", parameter.value);
                        input.setAttribute("min", parameter.minValue);
                        input.setAttribute("max", parameter.maxValue);

                        label.setAttribute("for", parameter.name);
                        label.innerHTML = parameter.name;

                        if (parameter.type == 'DOUBLE') {
                            input.setAttribute("step", 0.001);
                        } else {
                            input.setAttribute("step", 1);
                        }

                        input.required = true;

                        var errorDiv = document.createElement("div");
                        errorDiv.setAttribute("class", "help-block with-errors col-md-offset-1 col-xs-offset-6");

                        div.appendChild(label);
                        div.appendChild(inputDiv);
                        div.appendChild(errorDiv);

                        inputs.appendChild(div);

                        //set scale for graph
                        if (parameter.name == 'Max generations'
                            || parameter.name == 'Max. generations'
                            || parameter.name == 'maxGenerations') { //TODO this is even worse

                            graph.xMax = parameter.value;
                            graph.draw();
                        }
                    }

                    // it's a kind of magic
                    $('#trainingForm')
                        .validator('update');
                }
            }
        );
    }

    function validateTrainingForm(form) {
        var inputs = form.getElementsByTagName("input");

        var params = "?";
        var numberOfIterations = 1500; //default scale

        for (var i = 0; i < inputs.length; i++) {
            if (!inputs[i].checkValidity()) {
                return false;
            }

            if (inputs[i].name == 'btnSubmit') continue;

            params += inputs[i].name;
            params += "=";
            params += inputs[i].value;
            params += "&";

            if (inputs[i].name == 'Max generations'
                || inputs[i].name == 'Max. generations'
                || inputs[i].name == 'maxGenerations') { //TODO this is even worse
                numberOfIterations = inputs[i].value;
            }
        }

        startAlgorithm(params, numberOfIterations);

        return false;
    }

    function startAlgorithm(params, numberOfIterations) {
        graph.xMax = numberOfIterations;
        graph.draw();

        eventSource = new EventSource("train" + params);

        eventSource.onmessage = function(event) {
            if (event.data == "finished") {
                eventSource.close();
                return;
            }

            var result = JSON.parse(event.data);
            var iteration = result["iteration"];

            if (iteration == 0 || iteration == 1 || iteration % 5 == 0) {
                graph.addPoint([
                    new Point(iteration, result["best"]),
                    new Point(iteration, result["average"])
                ]);

                document.getElementById("fitnessDisplay").innerHTML = result["best"];
            }
        };
    }

    function stopAlgorithm() {
        $.ajax({
            url: "stopTraining",
            method: "POST",
            success: function () {
                eventSource.close();
            }
        });
    }

    function pauseAlgorithm() {
        $.ajax({
            url: "pauseTraining",
            method: "POST",
            success: function () {
                //TODO
            }
        });
    }

    function resumeAlgorithm() {
        $.ajax({
           url: "resumeTraining",
            method: "POST",
            success: function () {
                //TODO
            }
        });
    }

    function validateSimConfigForm(form) {
        var maxMoves = form["maxMoves"].value;
        var gridHeight = form["gridHeight"].value;
        var gridWidth = form["gridWidth"].value;
        var numberOfGrids = form["numberOfGrids"].value;
        var numberOfBottles = form["numberOfBottles"].value;
        var mapRegenFrequency = form["mapRegenFrequency"].value;
        var variableBottles = form["variableBottles"].checked;

        $.ajax({
            url: "simulatorConfig",
            method: "POST",
            data: {
                "maxMoves": maxMoves,
                "gridHeight": gridHeight,
                "gridWidth": gridWidth,
                "numberOfGrids": numberOfGrids,
                "numberOfBottles": numberOfBottles,
                "mapRegenFrequency": mapRegenFrequency,
                "variableBottles": variableBottles
            },
            dataType: "json",
            success: refreshSimulatorConfig
        });

        return false;
    }
</script>

<body onload="init()">

<jsp:include page="about.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-2">

            <%-- Algorithm selection --%>
            <div class="row">
                <div class="col-sm-13">
                    <div class="form-group">
                        <select onchange="optionSelected(this)" id="algorithmSelection" class="selectpicker form-control">
                            <option value="ga">Genetic algorithm</option>
                            <option value="nn">Neural network</option>
                            <option value="elman">Elman neural network</option>
                        </select>
                    </div>
                </div>
            </div>

            <%-- Algorithm parameters --%>
            <div class="row">
                <form id="trainingForm" class="form-horizontal" onsubmit="return validateTrainingForm(this)" data-toggle="validator" role="form">
                    <span id="trainingFormInputs"></span>

                    <input id="algorithmID" type="hidden" name="algorithmID">

                    <div class="form-group">
                        <div class="col-md-12">
                            <input type="submit" name="btnSubmit" value="Start algorithm" class="btn btn-default btn-lg btn-block">
                        </div>
                    </div>
                </form>
            </div>

            <%-- Simulator config --%>
            <div class="row">
                <form id="simulatorConfigForm" class="form-horizontal" onsubmit="return validateSimConfigForm(this);" data-toggle="validator" role="form">

                    <div class="form-group">
                        <label class="control-label col-sm-6" for="maxMoves">Max moves</label>
                        <div class="col-sm-6">
                            <input id="maxMoves" class="form-control" type="number" step="1" min="1" max="300" value="200" required/>
                        </div>
                        <div class="help-block with-errors col-md-offset-1 col-xs-offset-6"></div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-6" for="gridHeight">Grid height</label>
                        <div class="col-sm-6">
                            <input id="gridHeight" class="form-control" type="number" step="1" min="1" max="15" value="10" required/>
                        </div>
                        <div class="help-block with-errors col-md-offset-1 col-xs-offset-6"></div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-6" for="gridWidth">Grid width</label>
                        <div class="col-sm-6">
                            <input id="gridWidth" class="form-control" type="number" step="1" min="1" max="15" value="10" required/>
                        </div>
                        <div class="help-block with-errors col-md-offset-1 col-xs-offset-6"></div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-6" for="numberOfGrids">Number of grids</label>
                        <div class="col-sm-6">
                            <input id="numberOfGrids" class="form-control" type="number" step="1" min="1" max="200" value="200" required/>
                        </div>
                        <div class="help-block with-errors col-md-offset-1 col-xs-offset-6"></div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-6" for="numberOfBottles">Number of bottles</label>
                        <div class="col-sm-6">
                            <input id="numberOfBottles" class="form-control" type="number" step="1" min="1" max="225" value="50" required/>
                        </div>
                        <div class="help-block with-errors col-md-offset-1 col-xs-offset-6"></div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-sm-6" for="mapRegenFrequency">Map regen frequency</label>
                        <div class="col-sm-6">
                            <input id="mapRegenFrequency" class="form-control" type="number" step="1" min="0" max="10000" value="50" required/>
                        </div>
                        <div class="help-block with-errors col-md-offset-1 col-xs-offset-6"></div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-6 col-sm-10">
                            <div class="checkbox">
                                <label><input id="variableBottles" type="checkbox">Variable bottles</label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-12">
                            <input type="submit" name="btnSubmit" value="Configure simulator" class="btn btn-default btn-lg btn-block">
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <%-- Canvas --%>
        <div class="col-md-8">
            <div class="row">
                <canvas id="plotCanvas" class="img-responsive" width="1200" height="600">
                    Sorry, your browser does not support the canvas tag.
                </canvas>
            </div>

            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <div class="well">
                        <h4 class="text-center" id="fitnessDisplay">-</h4>
                    </div>
                </div>
            </div>
        </div>

        <%-- Training control --%>
        <div class="col-md-2">
            <button id="btnStop" class="btn btn-default btn-lg btn-block" type="button" onclick="stopAlgorithm()">Stop training</button>
            <button id="btnPause" class="btn btn-default btn-lg btn-block" type="button" onclick="pauseAlgorithm()">Pause training</button>
            <button id="btnResume" class="btn btn-default btn-lg btn-block" type="button" onclick="resumeAlgorithm()">Resume training</button>
            <button id="btnExport" class="btn btn-default btn-lg btn-block" type="button" onclick="location.href='exportRobot'">Export robot</button>
        </div>
    </div>
</div>
</body>
</html>
