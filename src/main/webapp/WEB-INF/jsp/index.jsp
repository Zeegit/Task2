<!DOCTYPE html>

<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>

<!-- <link rel="stylesheet" href="resources/style.min.css"/>
<script src="jquery.min.js"></script>
<script src="jstree.min.js"></script> -->

<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>

<body>
<h1>JSTree</h1>
<p>Use the context menu to add/changr/delete</p>
<!--<div><button onclick="addButton()">Add item to root</button></div>-->
<p></p>
<div></div>

<div id="jstree"></div>
</body>
<script>

    $(function () {
        getJSTree();
    });

    function getJSTree() {
        $('#jstree').jstree({
            core: {
                data: {
                    type: 'post',
                    url: '/get',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: function (node) {
                        return (node.id == '#') ? '0' : node.id;
                    }
                },
                check_callback: true
            },
            contextmenu: {
                items: function ($node) {
                    return {
                        createItem: {
                            "label": "Create",
                            "action": function (obj) {
                                addNode({text: 'New product', parentid: $node.id});
                            },
                            "_class": "class"
                        },
                        createItemRoot: {
                            "label": "Create root",
                            "action": function (obj) {
                                addNodeRoot({text: 'New product', parentid: '#'});
                            },
                            "_class": "class"
                        },
                        renameItem: {
                            "label": "Rename",
                            "action": function (obj) {
                                $('#jstree').jstree(true).edit($node);
                            }
                        },
                        deleteItem: {
                            "label": "Delete",
                            "action": function (obj) {
                                $('#jstree').jstree(true).delete_node($node);
                            }
                        }
                    };
                }
            },
            plugins: ['dnd', 'contextmenu', 'Wholerow', 'conditionalselect']
        })
            .bind('create_node.jstree', function (e, data) {
            })
            .bind('rename_node.jstree', function (e, data) {
                updateNode({id: data.node.id, parentid: data.node.parent, text: data.text});
            })
            .bind('delete_node.jstree', function (e, data) {
                deleteNode({id: data.node.id});
            })


            .bind('move_node.jstree', function (e, data) {
                var params = {
                    id: data.node.id,
                    parentid: data.node.parent,
                    text: data.node.text
                };
                updateNode(params);
            })
    }

    function addNodeRoot(treeObject) {
        $.ajax({
            url: '/add',
            data: JSON.stringify(treeObject),
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json',
            success: function (resp) {
                var parent = (resp.parentid === '0') ? '#' : resp.parentid;
                var node = {id: resp.id, text: resp.text};

                $('#jstree').jstree('create_node', parent, node, 'last');
                var id = resp.id
                $("#jstree").jstree('deselect_all');
                $("#jstree").jstree('select_node', id);
                var n = $("#jstree").jstree('get_selected');
                $("#jstree").jstree('edit', n[0]);


            }
        })
    }

    function addNode(treeObject) {
        $.ajax({
            url: '/add',
            data: JSON.stringify(treeObject),
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json',
            success: function (resp) {
                var parent = (resp.parentid === '0') ? '#' : resp.parentid;
                var node = {id: resp.id, text: resp.text};

                $('#jstree').jstree('create_node', parent, node, 'last');
                var id = resp.id
                $('#jstree').jstree('open_node', parent, function () {
                    $("#jstree").jstree('deselect_all');
                    $("#jstree").jstree('select_node', id);
                    var n = $("#jstree").jstree('get_selected');
                    $("#jstree").jstree('edit', n[0]);
                });


            }
        })
    }

    function updateNode(treeObject) {
        $.ajax({
            url: '/update',
            data: JSON.stringify(treeObject),
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json'
        })
    }

    function deleteNode(treeObject) {
        $.ajax({
            url: '/delete',
            data: JSON.stringify(treeObject),
            dataType: 'json',
            type: 'POST',
            contentType: 'application/json'
        })
    }

    function addButton() {
        addNode({parentid: '#', text: "New product"});
        $('#jstree').jstree(true).redraw();
    }


</script>
</html>