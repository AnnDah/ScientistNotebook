<?php
// This is a protected resource
if (session_status() == PHP_SESSION_ACTIVE) {
	echo "yabbadabbadoo";
} else {
	echo "pys";
}
?>