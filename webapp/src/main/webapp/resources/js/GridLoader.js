function GridLoader(canvas, width, height){
    var self = this;
    var context = canvas.getContext("2d");

    var grid = {"grid":[],"width":width,"height":height,"startX":0,"startY":0,"numberOfBottles":0};
    for (var i = 0; i < height; ++i){
        grid.grid.push([]);
        for (var j = 0; j < width; ++j){
            grid.grid[i].push("EMPTY");
        }
    }

    var tileWidth = canvas.width / grid.width;
    var tileHeight = canvas.height / grid.height;

    var imagesLoaded = 0;
    var grass = new Image();
    grass.onload = function() {
        imagesLoaded++;
    };
    var bottle = new Image();
    bottle.onload = function() {
        imagesLoaded++;
    };

    grass.src = "resources/pictures/grass.png";
    grass.alt = "Empty field with grass.";

    bottle.src = "resources/pictures/bottle.png";
    bottle.alt = "Field of grass with a bottle.";

    this.draw = function(){
        if (imagesLoaded != 2){
            setTimeout(self.draw, 100);
        }
        var posX = 0;
        var posY = 0;
        for (var i = 0; i < grid.height; ++i){
            for (var j = 0; j < grid.width; ++j){
                if (grid.grid[i][j] == "EMPTY"){
                    context.drawImage(grass, posX, posY, tileWidth - 1, tileHeight - 1);
                }
                if (grid.grid[i][j] == "BOTTLE"){
                    context.drawImage(bottle, posX, posY, tileWidth - 1, tileHeight - 1);
                }

                posX += tileWidth;
            }
            posX = 0;
            posY += tileHeight;
        }
    };
    self.draw();

    canvas.addEventListener('mousedown', function(evt) {
        var rect = canvas.getBoundingClientRect();
        var x = evt.clientX - rect.left;
        var y = evt.clientY - rect.top;

        var i = Math.floor(y / tileHeight);
        var j = Math.floor(x / tileWidth);

        if (grid.grid[i][j] == "EMPTY"){
            grid.grid[i][j] = "BOTTLE";
            grid.numberOfBottles++;
        } else {
            grid.grid[i][j] = "EMPTY";
            grid.numberOfBottles--;
        }

        self.draw();
    }, false);

    this.getGrid = function(){
        return grid;
    };

    this.setStartPosition = function(startX, startY){
         grid.startX = startX;
         grid.startY = startY;
    };
}