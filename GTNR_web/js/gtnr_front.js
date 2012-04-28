var graph = {
		'w':500, // px
		'h':500, // px
		'w_center':500 / 2, // px
		'h_center':500 / 2, // px
		'x_max':12000, // mm
		'y_max':12000 // mm
};

var rover = {
		'cell_radius_h':graph.h / 12, // px
		'cell_radius_w':graph.w / 10, // px
		'w':x_to_pixel(810), // mm
		'h':y_to_pixel(610), // mm
		'w_body':x_to_pixel(305), // mm
		'h_body':y_to_pixel(406) // mm
};

var sensors = {
		'ir':[0,0],
		'photocells':[0,0,0,0],
		'distances': get_random_array()
};

var atom = {
		'cpu':[0,0], // %
		'mem':0,	 // %
		'sleep':0,	 // %
		'alive':0,	 // %
		'io':0	 // %
};

var power = {
		'soc'    :100.0, // %
		'v_bat'  : 14.5, // V
		'v_panel': 18,   // V
		'c_bat'  :0,     // A
		'c_panel':0,     // A
		'w'      :0 	 // W
};

var return_obj;

function update_display() {
	var i;
	// TODO: get info through AJAX
	for(i=0;i<4;i++)
		sensors.photocells[i] = Math.floor(Math.random()*256);
	for(i=0;i<2;i++)
		sensors.ir[i] = Math.floor(Math.random()*1500);
	sensors.distances = get_random_array();
	generate_graph();
	// TODO: get info through AJAX
	for(i=0;i<2;i++)
		atom.cpu[i] = Math.floor(Math.random()*100);
	atom.mem = Math.floor(Math.random()*100);
	atom.sleep = Math.floor(Math.random()*50)+50;
	atom.awake = 100 - atom.sleep;
	atom.io = Math.floor(Math.random()*4)+2;
	power.soc -= 0.01;
	power.v_bat = Math.floor(Math.random()*1.5)+13;
	power.v_panel = Math.floor(Math.random()*1.5)+18;
	power.c_bat = Math.floor(Math.random()*0.8)+0.5;
	power.c_bat = Math.floor(Math.random()*0.2)+0.5;
	power.w = power.v_bat * power.c_bat;
	generate_stats();
	setTimeout("update_display();",200);
}

function update_from_server() {
	$.ajax("scripts/public_api.php").done(ret_update_from_server);
	setTimeout("update_from_server();",200);
}

function ret_update_from_server(data) {
	if (data) {
		data = jQuery.parseJSON(data);
		
		// atom
		atom.cpu[0] = data.atom.cpu1;
		atom.cpu[1] = data.atom.cpu2;
		atom.mem = data.atom.mem;
		atom.alive = data.atom.alive;
		atom.sleep = data.atom.sleep;
		atom.io = data.atom.io;
		
		// sensors
		sensors.distances = data.sensors.distances;
		sensors.ir = data.sensors.ir;
		sensors.photocells = data.sensors.photocells;
		
		// power
		power.soc = data.power.soc;
		power.v_bat = data.power.v_bat;
		power.v_panel = data.power.v_panel;
		power.c_bat = data.power.c_bat;
		power.c_panel = data.power.c_panel;
		power.w = power.v_bat * power.c_bat;
		

		generate_graph();
		generate_stats();
	}
}

function generate_stats() {
	var html = '<table border="1" cellspacing="0px" cellpadding="25px"><tr><td><pre>\n'+
	'Battery SOC         = '+power.soc+'% \n'+
	'Battery V           = '+power.v_bat+' V\n'+
	'Solar Panel V       = '+power.v_panel+' V\n'+
	'Battery Current     = '+power.c_bat+' A\n'+
	'Solar Panel Current = '+power.c_panel+' A\n'+
	'Total Power         = '+power.w+' W \n'+
	'</pre></td><td><pre>\n'+
	'Cpu 1  = '+atom.cpu[0]+'%\n'+ 
	'Cpu 2  = '+atom.cpu[1]+'%\n'+
	'Memory = '+atom.mem+'%\n'+
	'Program Alive Time = '+atom.alive+'%\n'+ 
	'Program Sleep Time = '+atom.sleep+'%\n'+ 
	'IO Wait Time = '+atom.io+'%\n'+
	'</pre></td></tr></table>';
	$('#analog_stats').html(html);
}

function generate_graph() {
	var IRL = place_IR(sensors.ir[0]);
	var IRR = place_IR(sensors.ir[1]);
	var html = '<svg id="graph" xmlns="http://www.w3.org/2000/svg" version="1.1"'+
		 		 'width="'+graph.w+'px" height="'+graph.h+'px"'+
				 'viewBox="0 0 '+graph.w+' '+graph.h+'">'+ 
		'<rect x="0" y="0" width="'+graph.w+'" height="'+graph.h+'"'+
			  'style="fill:gray;stroke:black;stroke-width:3;fill-opacity:0.1;'+
			  'stroke-opacity:0.9"/>'+
		'<defs>'+
			'<radialGradient id="grad0" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">'+
				'<stop offset="0%" style="stop-color:rgb('+(sensors.photocells[0])+','+(sensors.photocells[0])+','+(255-sensors.photocells[0])+');stop-opacity:1" />'+
				'<stop offset="100%" style="stop-color:#FFF;stop-opacity:0" />'+
		'</radialGradient>'+
	'</defs>'+
	'<defs>'+
			'<radialGradient id="grad1" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">'+
				'<stop offset="0%" style="stop-color:rgb('+(sensors.photocells[1])+','+(sensors.photocells[1])+','+(255-sensors.photocells[1])+');stop-opacity:1" />'+
				'<stop offset="100%" style="stop-color:#FFF;stop-opacity:0" />'+
			'</radialGradient>'+
	'</defs>'+
	'<defs>'+
			'<radialGradient id="grad2" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">'+
				'<stop offset="0%" style="stop-color:rgb('+(sensors.photocells[2])+','+(sensors.photocells[2])+','+(255-sensors.photocells[2])+');stop-opacity:1" />'+
				'<stop offset="100%" style="stop-color:#FFF;stop-opacity:0" />'+
			'</radialGradient>'+
	'</defs>'+
	'<defs>'+
			'<radialGradient id="grad3" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">'+
				'<stop offset="0%" style="stop-color:rgb('+(sensors.photocells[3])+','+(sensors.photocells[3])+','+(255-sensors.photocells[3])+');stop-opacity:1" />'+
				'<stop offset="100%" style="stop-color:#FFF;stop-opacity:0" />'+
			'</radialGradient>'+
	'</defs>'+
		// Photocells
		// front left
		'<ellipse cx="'+(graph.w_center - (rover.w/2) - 5)+'" cy="'+(graph.h_center - (rover.h/2) - 2)+'" rx="'+rover.cell_radius_w+'" ry="'+rover.cell_radius_w+'" fill="url(#grad0)" />'+
		// front right
		'<ellipse cx="'+(graph.w_center + (rover.w/2) + 5)+'" cy="'+(graph.h_center - (rover.h/2) - 2)+'" rx="'+rover.cell_radius_w+'" ry="'+rover.cell_radius_w+'" fill="url(#grad1)" />'+
		// back left
		'<ellipse cx="'+(graph.w_center - (rover.w/2) - 5)+'" cy="'+(graph.h_center + (rover.h/2) + 2)+'" rx="'+rover.cell_radius_w+'" ry="'+rover.cell_radius_w+'" fill="url(#grad2)" />'+
		// back right
		'<ellipse cx="'+(graph.w_center + (rover.w/2) + 5)+'" cy="'+(graph.h_center + (rover.h/2) + 2)+'" rx="'+rover.cell_radius_w+'" ry="'+rover.cell_radius_w+'" fill="url(#grad3)" />'+
		// solar panel
		'<rect x="'+(graph.w_center - (rover.w/2))+'" y="'+(graph.h_center - (rover.h/2))+'" width="'+rover.w+'" height="'+rover.h+'"'+
		      'style="stroke:black;stroke-width:1;fill-opacity:0;" />'+
		// body
		'<rect x="'+(graph.w_center - (rover.w_body/2))+'" y="'+(graph.h_center - (rover.h_body/2))+'" width="'+rover.w_body+'" height="'+rover.h_body+'"'+
		      'style="stroke:black;stroke-width:1;fill-opacity:0;" />'+
		// IR sensors
		'<rect x="'+(graph.w_center - (rover.w/2))+'" y="'+IRL.y+'" width="5" height="'+IRL.h+'"'+
		      'style="stroke:black;stroke-width:1;fill-opacity:0;" />'+
		'<rect x="'+(graph.w_center + (rover.w/2) - 5)+'" y="'+IRR.y+'" width="5" height="'+IRR.h+'"'+
		      'style="stroke:black;stroke-width:1;fill-opacity:0;" />'+
		// (0,0)
		'<rect x="250" y="250" width="2" height="2"'+
		      	'style="stroke:black;stroke-width:1;fill-opacity:0;" />';
	var i;
	for (i=0;i<sensors.distances.length;i++) {
		var p = angle_to_pixel(sensors.distances[i], i);
		html += '<ellipse cx="'+p.x+'" cy="'+p.y+'" rx="2" ry="2" fill="stroke:#000;" />';
	}
	html +='</svg>';
	$('#environment_display').html(html);
}

function x_to_pixel(x) {
	return ((graph.w * x) / graph.x_max);
}

function y_to_pixel(y) {
	return ((graph.h * y) / graph.y_max);
}

function point_to_pixel(p) {
	return { 'x':x_to_pixed(p.x), 'y':y_to_pixel(p.y) };
}

function angle_to_pixel(distance, degree) {
	degree = degree * 2 * Math.PI / 360;
	if (distance < 0)
		return { 'x':0, 'y':0 };
	var _y = -distance * Math.cos(degree);
	var _x = -distance * Math.sin(degree);
	_x = _x / graph.x_max;
	_y = _y / graph.y_max;
	return { 'x':(_x*graph.w)+(graph.w>>1), 'y':(_y*graph.h)+(graph.h>>1) };
}

function place_IR(dist) {
	var _y = y_to_pixel(dist);
	return { 'h':_y, 'y':(graph.h_center - (rover.h/2) - _y) };
}

function get_random_array() {
	var arr = new Array();
	var i;
	for(i=0;i<360;i++)
		arr[i] = (i*graph.x_max/360) + Math.floor(Math.random()*500) - 250;
	return arr;
}
