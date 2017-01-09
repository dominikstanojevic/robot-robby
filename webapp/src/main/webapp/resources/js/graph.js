function Graph(canvas, xAxis, yAxis, lineConfigs) {

    this.canvas = canvas;
    this.context = canvas.getContext("2d");

    this.xMin = xAxis.min;
    this.xMax = xAxis.max;
    this.xName = xAxis.name;

    this.yMin = yAxis.min;
    this.yMax = yAxis.max;
    this.yName = yAxis.name;

    this.lineConfigs = lineConfigs;

    var margins = {top: 10, down: 10, left: 30, right: 15};

    this.draw = function () {
        this.reset();

        var ctx = this.context;
        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

        ctx.beginPath();
        ctx.moveTo(margins.left, margins.top);
        ctx.lineTo(margins.left, this.canvas.height - margins.down);
        ctx.stroke();

        var yZero = (this.canvas.height - margins.top - margins.down) / (this.yMax - this.yMin) + margins.top;
        ctx.beginPath();
        ctx.moveTo(margins.left, yZero);
        ctx.lineTo(this.canvas.width - margins.right, yZero);
        ctx.stroke();

        ctx.font="13px Arial";

        ctx.textAlign = "right";
        ctx.textBaseline = "bottom";
        ctx.fillText(this.xName, this.canvas.width - margins.right, yZero - 2);

        ctx.textAlign = "left";
        ctx.textBaseline = "top";
        ctx.fillText(this.yName, margins.left + 2, margins.top);

        ctx.setLineDash([5, 3]);
        var oldColor = ctx.strokeStyle;
        ctx.strokeStyle = "#d2d2c7";

        var xMargin = (this.xMax - this.xMin) / 10;
        var yMargin = (this.yMax - this.yMin) / 15;

        //draw vertical lines
        for (var i = this.xMin; i <= this.xMax; i += xMargin) {
            if (Math.abs(i) < 1E-6) continue;

            var scaledX = scaleX(i);

            ctx.textBaseline = "top";
            ctx.textAlign = "center";
            ctx.fillText(i, scaledX, yZero);

            ctx.beginPath();
            ctx.moveTo(scaledX, margins.top);
            ctx.lineTo(scaledX, this.canvas.height - margins.down);
            ctx.stroke();
        }

        //draw horizontal lines
        for (var j = this.yMin; j <= this.yMax; j += yMargin) {
            var scaledY = scaleY(j);

            ctx.textBaseline = "middle";
            ctx.textAlign = "right";
            ctx.fillText(round(j, 2), margins.left - 4, scaledY);

            if (Math.abs(j) < 1E-6) continue;

            ctx.beginPath();
            ctx.moveTo(margins.left, scaledY);
            ctx.lineTo(this.canvas.width - margins.right, scaledY);
            ctx.stroke();
        }

        ctx.setLineDash([]);
        ctx.restore();
        ctx.strokeStyle = oldColor;
    };

    var that = this;

    var scaleX = function (x) {
        return (x / that.xMax) * (that.canvas.width - margins.left - margins.right) + margins.left;
    };
    var scaleY = function (y) {
        return ((that.yMax - y) / (that.yMax - that.yMin)) * (that.canvas.height - margins.top - margins.down) + margins.top;
    };

    this.prevPoints = [];

    this.addPoint = function (points) {
        var ctx = this.context;
        var oldColor = ctx.strokeStyle;

        if (this.prevPoints.length == 0) {
            for (var i = 0; i < points.length; i++) {
                var p = points[i];
                this.prevPoints.push(new Point(scaleX(p.x), scaleY(p.y)));
            }
            return;
        }

        for (var j = 0; j < points.length; j++) {
            var currentPoint = points[j];
            var prevPoint = this.prevPoints[j];

            var xPos = scaleX(currentPoint.x);
            var yPos = scaleY(currentPoint.y);

            ctx.strokeStyle = this.lineConfigs[j].color;

            ctx.beginPath();
            ctx.moveTo(prevPoint.x, prevPoint.y);
            ctx.lineTo(xPos, yPos);
            ctx.stroke();

            this.prevPoints[j] = new Point(xPos, yPos);
        }

        ctx.strokeStyle = oldColor;
    };

    this.reset = function () {
        this.prevPoints = [];
    };

    this.draw();
}

function Point(x, y) {
    this.x = x;
    this.y = y;
}

function round(number, decimals) {
    var base = Math.pow(10, decimals);
    return Math.round(number * base) / base;
}