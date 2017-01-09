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

    function init() {
        optionSelected();

        graph = new Graph(
            document.getElementById("plotCanvas"),
            { name: "Iterations", min: 0, max: 1000 },
            { name: "Fitness", min: -0.5, max: 1.0 },
            [
                { color: "Red" },
                { color: "Green" }
            ]
        );
    }

    function optionSelected() {
        var algorithmSelection = document.getElementById("algorithmSelection");
        var selected = algorithmSelection.options[algorithmSelection.selectedIndex].value;

        var table = document.getElementById("parametersTable");
        table.innerHTML = "";

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

                        var tableRow = document.createElement("tr");

                        var labelCell = document.createElement("th");
                        var inputCell = document.createElement("th");

                        var label = document.createElement("label");
                        var input = document.createElement("input");

                        input.setAttribute("type", "number");
                        input.setAttribute("name", parameter.name);
                        input.setAttribute("value", parameter.value);
                        input.setAttribute("min", parameter.minValue);
                        input.setAttribute("max", parameter.maxValue);

                        label.setAttribute("for", parameter.name);
                        label.innerHTML = parameter.name;

                        if (parameter.type == 'DOUBLE') {
                            input.setAttribute("step", 0.001);
                        }

                        tableRow.appendChild(labelCell);
                        tableRow.appendChild(inputCell);

                        labelCell.appendChild(label);
                        inputCell.appendChild(input);

                        table.appendChild(tableRow);
                    }
                }
            }
        );
    }

    function validateForm() {
        var inputs = document.forms["trainingForm"].getElementsByTagName("input");

        var params = "?";
        var numberOfIterations = 1000; //TODO this is bad

        for (var i = 0; i < inputs.length; i++) {
            if (!inputs[i].checkValidity()) {
                return false;
            }

            params += inputs[i].name;
            params += "=";
            params += inputs[i].value;
            params += "&";

            if (inputs[i].name == 'Max generations') { //TODO this is even worse
                numberOfIterations = inputs[i].value;
            }
        }

        startAlgorithm(params, numberOfIterations);

        return false;
    }

    function startAlgorithm(params, numberOfIterations) {
        graph.xMax = numberOfIterations;

        graph.draw();

        var eventSource = new EventSource("train" + params);
        eventSource.onmessage = function(event) {
            if (event.data == "finished") {
                eventSource.close();
                return;
            }

            var result = JSON.parse(event.data);
            var iteration = result["iteration"];

            if (iteration == 0 || iteration == 1 || iteration % 20 == 0) {
                graph.addPoint([
                    new Point(iteration, result["best"]),
                    new Point(iteration, result["average"])
                ]);

                document.getElementById("fitnessSpan").innerHTML = result["best"];
            }
        };
    }
</script>

<body onload="init()">

<jsp:include page="about.jsp"/>

<p><select onchange="optionSelected()" id="algorithmSelection">
    <option value="ga">Genetic algorithm</option>
    <option value="nn">Neural network</option>
</select></p>

<p><form id="trainingForm" method="post" onsubmit="return validateForm()">
    <table id="parametersTable">
    </table>

    <input id="algorithmID" type="hidden" name="algorithmID">
    <input type="submit">
</form>

<canvas id="plotCanvas" width="900" height="450"></canvas>
<span id="fitnessSpan"></span>

</body>
</html>
