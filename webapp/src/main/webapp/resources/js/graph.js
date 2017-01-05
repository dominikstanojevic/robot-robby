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

    var margin = 10;

    this.draw = function () {
        var ctx = this.context;
        ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

        ctx.beginPath();
        ctx.moveTo(margin, margin);
        ctx.lineTo(margin, this.canvas.height - margin);
        ctx.stroke();

        var yZero = this.canvas.height / (this.yMax - this.yMin);
        ctx.beginPath();
        ctx.moveTo(margin, yZero);
        ctx.lineTo(this.canvas.width - margin, yZero);
        ctx.stroke();
    };

    var that = this;

    var scaleX = function (x) {
        return (x / that.xMax) * (that.canvas.width - 2 * margin) + margin;
    };
    var scaleY = function (y) {
        return ((that.yMax - y) / (that.yMax - that.yMin)) * (that.canvas.height - 2 * margin) + margin;
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

    this.draw();
}

function Point(x, y) {
    this.x = x;
    this.y = y;
}