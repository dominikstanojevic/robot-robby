<html>
<head>
    <title>Index</title>
    <meta charset="utf-8">

    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>


    <script type="text/javascript" src="resources/js/graph.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
</head>

<script type="text/javascript">

    var graph;
    var eventSource;

    function init() {
        optionSelected();

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

                        label.setAttribute("class", "control-label col-sm-3");
                        input.setAttribute("class", "form-control");

                        inputDiv.setAttribute("class", "col-sm-9");

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

                        div.appendChild(label);
                        div.appendChild(inputDiv);

                        inputs.appendChild(div);

                        //set scale for graph
                        if (parameter.name == 'Max generations'
                            || parameter.name == 'Max. generations'
                            || parameter.name == 'maxGenerations') { //TODO this is even worse

                            graph.xMax = parameter.value;
                            graph.draw();
                        }
                    }
                }
            }
        );
    }

    function validateTrainingForm() {
        var inputs = document.forms["trainingForm"].getElementsByTagName("input");

        var params = "?";
        var numberOfIterations = 1000; //TODO this is bad

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

                document.getElementById("fitnessSpan").innerHTML = result["best"];
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
</script>

<body onload="init()">

<jsp:include page="about.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-4">
            <div class="row">
                <div class="col-sm-offset-3 col-sm-6">
                    <select onchange="optionSelected()" id="algorithmSelection" class="selectpicker" data-width="auto">
                        <option value="ga">Genetic algorithm</option>
                        <option value="nn">Neural network</option>
                        <option value="elman">Elman neural network</option>
                    </select>
                </div>
            </div>

            <br>
            <div class="row">
                <form id="trainingForm" class="form-horizontal" onsubmit="return validateTrainingForm()">
                    <span id="trainingFormInputs"></span>

                    <input id="algorithmID" type="hidden" name="algorithmID">

                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <input type="submit" name="btnSubmit" value="Start algorithm" class="btn btn-default">
                        </div>
                    </div>
                </form>
            </div>

        <div class="col-sm-8">
            <canvas id="plotCanvas" class="img-responsive" width="1200" height="600">
                Sorry, your browser does not support the canvas tag.
            </canvas>
            <br><span id="fitnessSpan"></span>

            <button id="btnStop" class="btn btn-default" type="button" onclick="stopAlgorithm()">Stop training</button>
            <button id="btnPause" class="btn btn-default" type="button" onclick="pauseAlgorithm()">Pause training</button>
            <button id="btnResume" class="btn btn-default" type="button" onclick="resumeAlgorithm()">Resume training</button>
        </div>
    </div>
</div>
</body>
</html>
