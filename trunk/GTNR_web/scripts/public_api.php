<?php

$dist_filename = "log/dist.log";
$sensors_filename = "log/sensors.log";
$power_filename = "log/power.log";
$atom_filename = "log/atom.log";

class Atom {
	public $cpu1;
	public $cpu2;
	public $mem;
	public $sleep;
	public $alive;
	public $io;
	public function __construct() {
	}
	public function get_array() {
		return array("cpu1"=>$this->cpu1, "cpu2"=>$this->cpu2, "mem"=>$this->mem, "sleep"=>$this->alive, "io"=>$this->io);
	}
	public function get_json() {
		return  json_encode(get_array());
	}
}

class Power {
	public $soc;
	public $v_bat;
	public $v_panel;
	public $c_bat;
	public $c_panel;
	public function __construct() {
	}
	public function get_array() {
		return array("soc"=>$this->soc, "v_bat"=>$this->v_bat, "v_panel"=>$this->v_panel, "c_bat"=>$this->c_bat, "c_panel"=>$this->c_panel);
	}
	public function get_json() {
		return  json_encode(get_array());
	}
}

class Sensors {
	public $ir;
	public $photocells;
	public $distances;
	public function __construct() {
		$this->ir = array(0,0);
		$this->photocells = array(0,0,0,0);
		$this->distances = array();
		for ($i=0;$i<360;$i++)
			$this->distances[] = 0;
	}
	public function get_array() {
		return array("ir"=>$this->ir, "photocells"=>$this->photocells, "distances"=>$this->distances);
	}
	public function get_json() {
		return  json_encode(get_array());
	}
}

function get_data() {
	// Sensors
	$sensors = new Sensors();
	$file_handle = fopen($dist_filename, "r");
	$i = 0;
	for ($i=0;$i<360;$i++)
		$sensors->distances[$i++] = intval(fgets($file_handle));
	fclose($file_handle);
	$file_handle = fopen($sensors_filename, "r");
	$sensors->ir[0] = intval(fgets($file_handle));
	$sensors->ir[1] = intval(fgets($file_handle));
	for ($i=0;$i<4;$i++)
		$sensors->photocells[$i] = intval(fgets($file_handle));
	fclose($file_handle);
	
	// Power
	$power = new Power();
	$file_handle = fopen($power_filename, "r");
	$power->soc = intval(fgets($file_handle));
	$power->v_bat = intval(fgets($file_handle));
	$power->v_panel = intval(fgets($file_handle));
	$power->c_bat = intval(fgets($file_handle));
	$power->c_panel = intval(fgets($file_handle));
	fclose($file_handle);
	
	// Atom
	$atom = new Atom();
	$file_handle = fopen($atom_filename, "r");
	$atom->cpu1 = intval(fgets($file_handle));
	$atom->cpu2 = intval(fgets($file_handle));
	$atom->mem = intval(fgets($file_handle));
	$atom->alive = intval(fgets($file_handle));
	$atom->sleep = intval(fgets($file_handle));
	$atom->io = intval(fgets($file_handle));
	fclose($file_handle);
	
	echo json_encode(array("sensors"=>$sensors, "power"=>$power, "atom"=>$atom));
}

get_data();

?>