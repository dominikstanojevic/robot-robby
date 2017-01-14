function Simulation(canvas, grid, moves){
    var self = this;

    this.delay = 500;

    var context = canvas.getContext("2d");
    var grids = [];

    var tileWidth = canvas.width / grid.width;
    var tileHeight = canvas.height / grid.height;

    var robyX = grid.startX;
    var robyY = grid.startY;
    var performingPickup = false;
    var isWallHit = false;
    var firstMove = true;

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

    var moveIndex = 0;
    this.draw = function(){
        context.fillRect(0, 0, canvas.width, canvas.height);

        if (imagesLoaded != 8){
            setTimeout(self.draw, 100);
        }

        var posX = 0;
        var posY = 0;

        for (var i = 0; i < grid.height; ++i){
            for (var j = 0; j < grid.width; ++j){

                if (robyX == i && robyY == j){
                    if (performingPickup == true){
                        if (grid.grid[i][j] == "EMPTY"){
                            context.drawImage(emptyPickup, posX, posY, tileWidth - 1, tileHeight - 1);
                        }
                        if (grid.grid[i][j] == "BOTTLE"){
                            context.drawImage(pickup, posX, posY, tileWidth - 1, tileHeight - 1);
                        }
                    } else if (isWallHit == true) {
                        if (grid.grid[i][j] == "EMPTY"){
                            context.drawImage(wallHit, posX, posY, tileWidth - 1, tileHeight - 1);
                        }
                        if (grid.grid[i][j] == "BOTTLE"){
                            context.drawImage(bottleWall, posX, posY, tileWidth - 1, tileHeight - 1);
                        }
                    } else {
                        if (grid.grid[i][j] == "EMPTY"){
                            context.drawImage(roby, posX, posY, tileWidth - 1, tileHeight - 1);
                        }
                        if (grid.grid[i][j] == "BOTTLE"){
                            context.drawImage(robyBottle, posX, posY, tileWidth - 1, tileHeight - 1);
                        }
                    }
                } else {
                    if (grid.grid[i][j] == "EMPTY"){
                        context.drawImage(grass, posX, posY, tileWidth - 1, tileHeight - 1);
                    }
                    if (grid.grid[i][j] == "BOTTLE"){
                        context.drawImage(bottle, posX, posY, tileWidth - 1, tileHeight - 1);
                    }
                }

                posX += tileWidth;
            }
            posX = 0;
            posY += tileHeight;

            context.rect(0, 0, canvas.width, canvas.height);
            context.stroke();
        }
    };

    var toDo = null;
    this.next = function(){
        firstMove = false;

        if (toDo != null){
            grid.grid[toDo.x][toDo.y] = "EMPTY";
            toDo = null;
        }

        if (moveIndex >= moves.length){
            clearInterval(interval);
            return;
        }

        if (moveIndex >= grids.length){
            grid.startX = robyX;
            grid.startY = robyY;

            var newGrid = JSON.stringify(grid);
            newGrid = JSON.parse(newGrid);
            grids.push(newGrid);
        }
        var move = moves[moveIndex];

        performingPickup = false;
        isWallHit = false;
        if (move == "UP"){
            robyX--;
            if (robyX < 0){
                robyX = 0;
                isWallHit = true;
            }
        } else if (move == "DOWN"){
            robyX++;
            if (robyX >= grid.height){
                robyX = grid.height - 1;
                isWallHit = true;
            }
        } else if (move == "LEFT"){
            robyY--;
            if (robyY < 0){
                robyY = 0;
                isWallHit = true;
            }
        } else if (move == "RIGHT"){
            robyY++;
            if (robyY >= grid.width){
                robyY = grid.width - 1;
                isWallHit = true;
            }
        } else if (move == "COLLECT"){
            if (grid.grid[robyX][robyY] == "BOTTLE"){
                toDo = {"x":robyX, "y":robyY};
            }
            performingPickup = true;
        }

        moveIndex++;
        self.draw();
    };

    var interval;
    this.play = function(){
        interval = setInterval(self.next, self.delay);
    };

    this.pause = function(){
        clearInterval(interval);
    };

    this.previous = function(){
        if (firstMove) return;

        self.pause();
        moveIndex -= 2;
        toDo = null;

        if (moveIndex < 0){
            moveIndex = 0;
            grid = JSON.stringify(grids[moveIndex]);
            grid = JSON.parse(grid);
            robyX = grid.startX;
            robyY = grid.startY;
            self.draw();
        } else {
            grid = JSON.stringify(grids[moveIndex]);
            grid = JSON.parse(grid);
            robyX = grid.startX;
            robyY = grid.startY;
            self.next();
        }
    };

    this.reset = function(){
        if (firstMove) return;
        self.pause();
        toDo = null;

        moveIndex = 0;
        performingPickup = false;
        isWallHit = false;

        grid = JSON.stringify(grids[moveIndex]);
        grid = JSON.parse(grid);
        robyX = grid.startX;
        robyY = grid.startY;
        self.draw();
    };
}