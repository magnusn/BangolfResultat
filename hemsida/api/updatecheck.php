<?php

// Parse without sections
$ini_array = parse_ini_file("bangolfresultat.ini");
echo "release {$ini_array['application.version']}"

?>
