//为表单元素赋值
var setValue = function(name, val){
    if(val != ""){

        var htmlType = $("[name='"+name+"']").attr("type");

        if(htmlType == "text" || htmlType == "textarea" || htmlType == "select-one" || htmlType == "hidden" || htmlType == "button"){

            $("[name='"+name+"']").val(val);

        }else if(htmlType == "radio"){

            $("input[type=radio][name='"+name+"'][value='"+val+"']").attr("checked",true);

        }else if(htmlType == "checkbox"){

            var vals = val.split(",");

            for(var i=0; i < vals.length; i++){

                $("input[type=checkbox][name='"+name+"'][value='"+vals[i]+"']").attr("checked",true);

            }

        }

    }

};