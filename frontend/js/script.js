$(document).on('click', '#start', function(event){
    event.preventDefault();

    $('html, body').animate({
        scrollTop: $( $.attr(this, 'href') ).offset().top
    }, 500);
});
function addRow() {
	var url = document.getElementById("url");
	var type = document.getElementById("type");
	var duration = document.getElementById("duration");
	var n = document.getElementById("n");
	var m = document.getElementById("m");

    var table = document.getElementById("old_events");
    var row = table.insertRow(1);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
    var cell5 = row.insertCell(4);
    cell1.innerHTML = url.value;
    cell2.innerHTML = type.value;
    cell3.innerHTML = duration.value;
    cell4.innerHTML = n.value;
    cell5.innerHTML = m.value;

    //Clear input
    url.value ="";
    type.selectedIndex=0
    duration.value ="";
    n.value ="";
    m.value ="";
};

function generalagraella(){
	var n = document.getElementById("n").value;
	var m = document.getElementById("m").value;
	var table = document.getElementById("lagraella");
	for(i = 0; i < n; ++i){
		var row = table.insertRow(i);
		for (j = 0; j < m; ++j){
    		var cell = row.insertCell(j);
		}
		"<br>"
	}
	"<br>"
}
function clicking() {}

$('#submit22').on('click',function(){

    var $url = $('#url');
    var $duration  = $('#duration');
    var $rows = $('#rows'); 
    var $columns = $('#columns');

    var post = {
        "url": $url.val(),
        "duration": $duration.val(),
        "type" : "static",
        "m": parseInt($columns.val()),
        "n": parseInt($rows.val())
    }
    console.log(post);
    $.ajax({
        type:'POST',
        url:'http://54.245.32.155:8080/add_figure',
        data: post,
        success: function(){
            var $preview = $('#main-img');
            $preview.attr("src",$url.val());
        },
        error: function(){
            alert('error');
        }
    })
})
