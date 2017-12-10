<?php

//echo $_POST["id"];
//echo $_POST["data"];

$id = $_POST['id'];
$post_data = $_POST['data'];
if (!empty($post_data)) {
    if (!file_exists('save/' . $id . '/pdf.pdf')) {
        mkdir('save/' . $id . '/pdf.pdf', 0777, true);
    }
    $dir = 'save/' . $id . '/pdf.pdf';
    $file = uniqid() . getmypid();
    $filename = $dir . $file . '.pdf';
    $handle = fopen($filename, "w");
    fwrite($handle, $post_data);
    fclose($handle);
    echo $file;
}
//$target_dir = "save/";
//$target_file = $target_dir . basename($_FILES["data"]["name"]);
//$namaFile = $_FILES["data"]["name"];
//$target_file = $target_dir . basename($namamd5);
//move_uploaded_file($_FILES["data"]["tmp_name"], $target_file);
?>