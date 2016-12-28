<html>
<head>
    <title>Index</title>
    <meta charset="utf-8">
</head>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript">

    function init() {
        optionSelected();
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
</script>

<body onload="init()">

<p><select onchange="optionSelected()" id="algorithmSelection">
    <option value="ga">Genetic algorithm</option>
    <option value="nn">Neural network</option>
</select></p>

<p><form id="trainingForm" method="get" action="train">
    <table id="parametersTable">
    </table>

    <input id="algorithmID" type="hidden" name="algorithmID">
    <input type="submit" value="Submit">
</form></p>

<p><canvas id="canvas" width="400" height="300" style="border: solid black 1px"></canvas></p>

</body>
</html>
