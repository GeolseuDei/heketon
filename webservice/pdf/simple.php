<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Simple example</title>
    </head>
    <style>
        html {
            box-sizing: border-box;
        }

        *, *:before, *:after {
            box-sizing: inherit;
        }
    </style>
    <body>
        <button onclick="generate()">Generate pdf</button>

        <script src="libs/jspdf.debug.js"></script>
        <script src="libs/jspdf.plugin.autotable.js"></script>

        <table id="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>First name</th>
                    <th>Last name</th>
                    <th>Email</th>
                    <th>Country</th>
                    <th>IP-address</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1</td>
                    <td>Donna</td>
                    <td>Moore</td>
                    <td>dmoore0@furl.net</td>
                    <td>China</td>
                    <td>211.56.242.221</td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>Janice</td>
                    <td>Henry</td>
                    <td>jhenry1@theatlantic.com</td>
                    <td>Ukraine</td>
                    <td>38.36.7.199</td>
                </tr>
            </tbody>
        </table>


    </body>
</html>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script>
            function generate() {
                var columns = ["ID", "Country", "Rank", "Capital"];
                var data = [
                    [1, "Denmark", 7.526, "Copenhagen"],
                    [2, "Switzerland", 7.509, "Bern"],
                    [3, "Iceland", 7.501, "Reykjavík"],
                    [4, "Norway", 7.498, "Oslo"],
                    [5, "Finland", 7.413, "Helsinki"]
                ];

                var doc = new jsPDF();
                doc.autoTable(columns, data);
                var pdf = doc.output();

                $.post("./upload.php", {data: pdf, id: 1})
                        .done(function (data) {
                            alert("pengiriman data pdf berhasil " + data);
                        })
                        .fail(function () {
                            alert("pengiriman data pdf gagal");
                        })
//
//        $.post("./dummy.php", {id: 5}).done(function (data) {
//            alert("dummy1");
//        }).fail(function () {
//            alert("dummy2");
//        })
//                var data = new FormData();
//                data.append("data", pdf);
//                var xhr = new XMLHttpRequest();
//                xhr.open('post', './upload.php', true);
//                xhr.send(data);

                //        doc.save('save/table.pdf');
                //        file_put_contents("Tmpfile.zip", file_get_contents(doc.save('table.pdf')));
                //        file_put_contents("save/table.pdf", fopen(doc.save('table.pdf'), 'r'));

                //        doc.output("dataurlnewwindow");
            }
</script>