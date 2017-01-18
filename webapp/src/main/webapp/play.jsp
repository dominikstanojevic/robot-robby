<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Robot Robby - Play</title>
    <meta charset="utf-8">

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css"/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>

    <script type="text/javascript" src="resources/js/bootstrap-filestyle.min.js"> </script>

    <script type="text/javascript">
        var canvas =  null;
        var context = null;
        var grid = null;
        var gridCopy = null;

        function init(){
            var link = document.getElementById("navItemPlay");
            link.setAttribute("class", "active");

            canvas = document.getElementById("canvas");
            context = canvas.getContext("2d");

            tileWidth = canvas.width / 3;
            tileHeight = canvas.height / 3;

            generateMap();
        }

        var tileWidth;
        var tileHeight;

        var robyX;
        var robyY;
        var performingPickup = false;
        var isWallHit = false;
        var score = 0;
        var moveIndex = 0;

        var imagesLoaded = 0;
        var grass = new Image();
        grass.onload = function() {
            imagesLoaded++;
        };
        var bottle = new Image();
        bottle.onload = function() {
            imagesLoaded++;
        };
        var roby = new Image();
        roby.onload = function() {
            imagesLoaded++;
        };
        var pickup = new Image();
        pickup.onload = function() {
            imagesLoaded++;
        };
        var emptyPickup = new Image();
        emptyPickup.onload = function() {
            imagesLoaded++;
        };
        var robyBottle = new Image();
        robyBottle.onload = function() {
            imagesLoaded++;
        };
        var wallHit = new Image();
        wallHit.onload = function() {
            imagesLoaded++;
        };
        var bottleWall = new Image();
        bottleWall.onload = function() {
            imagesLoaded++;
        };
        var wall = new Image();
        wall.onload = function(){
            imagesLoaded++;
        };

        grass.src = "resources/pictures/grass.png";
        grass.alt = "Empty field with grass.";

        bottle.src = "resources/pictures/bottle.png";
        bottle.alt = "Field of grass with a bottle.";

        roby.src = "resources/pictures/roby.png";
        roby.alt = "Field with robot.";

        pickup.src = "resources/pictures/pickup.png";
        pickup.alt = "Robot picking up a bottle.";

        emptyPickup.src = "resources/pictures/empty-pickup.png";
        emptyPickup.alt = "Robot trying to perform a pickup on empty.";

        robyBottle.src = "resources/pictures/roby-bottle.png";
        robyBottle.alt = "Robot standing on the same field as a bottle.";

        wallHit.src = "resources/pictures/roby-wall.png";
        wallHit.alt = "Robot after hitting a wall.";

        bottleWall.src = "resources/pictures/roby-bottle-wall.png";
        bottleWall.alt = "Robot standing on a bottle and hitting a wall.";

        wall.src = "resources/pictures/wall.png";
        wall.alt = "Wall.";

        draw = function(){
            if (imagesLoaded != 9){
                setTimeout(self.draw, 100);
            }

            var i = robyX - 1;
            var j = robyY;

            if (i < 0){
                context.drawImage(wall, tileWidth, 0, tileWidth, tileHeight);
            } else if (grid.grid[i][j] == "EMPTY"){
                context.drawImage(grass, tileWidth, 0, tileWidth, tileHeight);
            } else if (grid.grid[i][j] == "BOTTLE"){
                context.drawImage(bottle, tileWidth, 0, tileWidth, tileHeight);
            }

            i = robyX;
            j = robyY - 1;

            if (j < 0){
                context.drawImage(wall, 0, tileHeight, tileWidth, tileHeight);
            } else if (grid.grid[i][j] == "EMPTY"){
                context.drawImage(grass, 0, tileHeight, tileWidth, tileHeight);
            } else if (grid.grid[i][j] == "BOTTLE"){
                context.drawImage(bottle, 0, tileHeight, tileWidth, tileHeight);
            }

            i = robyX;
            j = robyY + 1;

            if (j >= grid.width){
                context.drawImage(wall, 2 * tileWidth, tileHeight, tileWidth, tileHeight);
            } else if (grid.grid[i][j] == "EMPTY"){
                context.drawImage(grass, 2 * tileWidth, tileHeight, tileWidth, tileHeight);
            } else if (grid.grid[i][j] == "BOTTLE"){
                context.drawImage(bottle, 2 * tileWidth, tileHeight, tileWidth, tileHeight);
            }

            i = robyX + 1;
            j = robyY;

            if (i >= grid.height){
                context.drawImage(wall, tileWidth, 2 * tileHeight, tileWidth, tileHeight);
            } else if (grid.grid[i][j] == "EMPTY"){
                context.drawImage(grass, tileWidth, 2 * tileHeight, tileWidth, tileHeight);
            } else if (grid.grid[i][j] == "BOTTLE"){
                context.drawImage(bottle, tileWidth, 2 * tileHeight, tileWidth, tileHeight);
            }

            i = robyX;
            j = robyY;
            if (performingPickup == true){
                if (grid.grid[i][j] == "EMPTY"){
                    context.drawImage(emptyPickup, tileWidth, tileHeight, tileWidth, tileHeight);
                }
                if (grid.grid[i][j] == "BOTTLE"){
                    context.drawImage(pickup, tileWidth, tileHeight, tileWidth, tileHeight);
                }
            } else if (isWallHit == true) {
                if (grid.grid[i][j] == "EMPTY"){
                    context.drawImage(wallHit, tileWidth, tileHeight, tileWidth, tileHeight);
                }
                if (grid.grid[i][j] == "BOTTLE"){
                    context.drawImage(bottleWall, tileWidth, tileHeight, tileWidth, tileHeight);
                }
            } else {
                if (grid.grid[i][j] == "EMPTY"){
                    context.drawImage(roby, tileWidth, tileHeight, tileWidth, tileHeight);
                }
                if (grid.grid[i][j] == "BOTTLE"){
                    context.drawImage(robyBottle, tileWidth, tileHeight, tileWidth, tileHeight);
                }
            }

            updateMoves();
        };

        var toDo = null;
        nextMove = function(move){
            if (moveIndex >= 200){
                return;
            }

            if (toDo != null){
                grid.grid[toDo.x][toDo.y] = "EMPTY";
                toDo = null;
            }

            performingPickup = false;
            isWallHit = false;
            if (move == "UP"){
                robyX--;
                if (robyX < 0){
                    robyX = 0;
                    isWallHit = true;
                    score -= 5;
                }
            } else if (move == "DOWN"){
                robyX++;
                if (robyX >= grid.height){
                    robyX = grid.height - 1;
                    isWallHit = true;
                    score -= 5;
                }
            } else if (move == "LEFT"){
                robyY--;
                if (robyY < 0){
                    robyY = 0;
                    isWallHit = true;
                    score -= 5;
                }
            } else if (move == "RIGHT"){
                robyY++;
                if (robyY >= grid.width){
                    robyY = grid.width - 1;
                    isWallHit = true;
                    score -= 5;
                }
            } else if (move == "COLLECT"){
                if (grid.grid[robyX][robyY] == "BOTTLE"){
                    toDo = {"x":robyX, "y":robyY};
                    score += 10;
                } else {
                    score -= 1;
                }
                performingPickup = true;
            }

            draw();
            moveIndex++;
            if (moveIndex == 200){
                endGame();
            }
        };

        goUp = function(){
            nextMove("UP");
        };

        goDown = function(){
            nextMove("DOWN");
        };

        goLeft = function(){
            nextMove("LEFT");
        };

        goRight = function(){
            nextMove("RIGHT");
        };

        collect = function(){
            nextMove("COLLECT");
        };

        function endGame(){
            var data = new FormData();
            data.append("grid", JSON.stringify(gridCopy));

            $.ajax({
                url: "./getStats",
                method: "POST",
                data: data,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    document.getElementById("disp2").innerHTML = data[0];
                    document.getElementById("disp3").innerHTML = data[1];
                    document.getElementById("disp4").innerHTML = data[2];
                    document.getElementById("disp5").innerHTML = data[3];
                    document.getElementById("disp6").innerHTML = data[4];
                }
            });

            updateMoves();
        }

        function generateMap(){
            $.ajax({
                url: "./newGrid",
                method: "POST",
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    grid = data;
                    gridCopy = JSON.stringify(grid);
                    gridCopy = JSON.parse(gridCopy);

                    robyX = grid.startX;
                    robyY = grid.startY;
                    moveIndex = 0;
                    toDo = null;
                    score = 0;
                    updateMoves();

                    document.getElementById("disp1").innerHTML = "";
                    document.getElementById("disp2").innerHTML = "";
                    document.getElementById("disp3").innerHTML = "";
                    document.getElementById("disp4").innerHTML = "";
                    document.getElementById("disp5").innerHTML = "";
                    document.getElementById("disp6").innerHTML = "";
                    draw();
                }
            });
        }

        var updateMoves = function(){
            var moveString = "";
            moveString += moveIndex;
            moveString += "/";
            moveString += 200;

            document.getElementById("moveDisplay").innerHTML = moveString;
            document.getElementById("disp1").innerHTML = "Your score: " + score;
        };

        var detectKey = function(e){
            e = e || window.event;

            if (e.keyCode == '87') {
                goUp();
            }
            else if (e.keyCode == '83') {
                goDown();
            }
            else if (e.keyCode == '65') {
                goLeft();
            }
            else if (e.keyCode == '68') {
                goRight();
            } else if (e.keyCode == '74'){
                collect();
            }
        };
        document.onkeydown = detectKey;

    </script>

</head>
<body onload="init()">
<jsp:include page="navbar.jsp"/>

<div class="container-fluid" style="margin: 10px 10px 10px 10px">
    <div class="row">
        <div class="col-md-7 col-md-offset-1">
            <canvas id="canvas" width="1024" height="768" class="img-responsive"></canvas>
        </div>

        <div class="col-md-4">
            <div class="row">
                <div class="col-md-12">
                    <h4 class="text-center" id="moveDisplay">/</h4>
                </div>
            </div>

            <br/>

            <div class="row">
                <div class="col-md-3 col-md-offset-4">
                    <button id = "btnUp" onclick="goUp()" class="btn btn-default btn-block">
                        Up
                    </button>
                </div>

                <div class="col-md-3">
                    <button id = "btnCollect" onclick="collect()" class="btn btn-default btn-block">
                        Collect
                    </button>
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="col-md-3 col-md-offset-1">
                    <button id = "btnLeft" onclick="goLeft()" class="btn btn-default btn-block">
                        Left
                    </button>
                </div>
                <div class="col-md-3">
                    <button id = "btnDown" onclick="goDown()" class="btn btn-default btn-block">
                        Down
                    </button>
                </div>
                <div class="col-md-3">
                    <button id = "btnRight" onclick="goRight()" class="btn btn-default btn-block">
                        Right
                    </button>
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="col-md-9 col-md-offset-1">
                    <button id = "btnReset" onclick="init()" class="btn btn-default btn-block">
                        Reset
                    </button>
                </div>
            </div>
            <br/>
            <br/>

            <div class="row">
                <div class="col-md-12">
                    <h4 class="text-center" id="disp1"></h4>
                </div>
                <br/>
                <div class="col-md-12">
                    <h4 class="text-center" id="disp2"></h4>
                </div>
                <div class="col-md-12">
                    <h4 class="text-center" id="disp3"></h4>
                </div>
                <div class="col-md-12">
                    <h4 class="text-center" id="disp4"></h4>
                </div>
                <div class="col-md-12">
                    <h4 class="text-center" id="disp5"></h4>
                </div>
                <div class="col-md-12">
                    <h4 class="text-center" id="disp6"></h4>
                </div>
            </div>

        </div>
    </div>
</div>
</body>
</html>
