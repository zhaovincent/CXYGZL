(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3c48e96a"],{"0d29":function(e,t,o){"use strict";o.r(t);var l=function(){var e=this,t=e.$createElement,o=e._self._c||t;return o("div",["DESIGN"===e.mode?o("div",[e.expanding?o("el-radio-group",{model:{value:e._value,callback:function(t){e._value=t},expression:"_value"}},e._l(e.optionsShow,(function(t,l){return o("el-radio",{key:l,attrs:{disabled:"",label:t.key}},[e._v(e._s(t.value))])})),1):o("el-select",{staticClass:"max-fill",attrs:{size:"medium",disabled:"",placeholder:e.placeholder},model:{value:e._value,callback:function(t){e._value=t},expression:"_value"}})],1):o("div",[e.expanding?o("el-radio-group",{model:{value:e._value,callback:function(t){e._value=t},expression:"_value"}},e._l(e.optionsShow,(function(t,l){return o("el-radio",{key:l,attrs:{disabled:e.perm&&"R"===e.perm,label:t.key}},[e._v(" "+e._s(t.value)+" ")])})),1):o("el-select",{staticClass:"max-fill",attrs:{size:"medium",clearable:!e.perm||"E"===e.perm,disabled:e.perm&&"R"===e.perm,placeholder:e.placeholder},model:{value:e._value,callback:function(t){e._value=t},expression:"_value"}},e._l(e.optionsShow,(function(e,t){return o("el-option",{key:t,attrs:{value:e.key,label:e.value}})})),1)],1)])},n=[],a=o("8f73"),r=o("c7ea"),i={mixins:[a["a"]],name:"SelectInput",components:{},props:{value:{type:String,default:null},placeholder:{type:String,default:"请选择选项"},expanding:{type:Boolean,default:!1},options:{type:Array,default:function(){return[]}},perm:{type:String,default:"E"},optionsFromRemote:{type:Boolean,default:!1},remoteUrl:{type:String,default:""}},data:function(){return{remoteOptions:[]}},computed:{optionsShow:function(){return this.optionsFromRemote&&this.remoteUrl.length>0?this.remoteOptions:this.options}},mounted:function(){var e=this;this.optionsFromRemote&&this.remoteUrl.length>0&&"DESIGN"!=this.mode&&Object(r["a"])({remoteUrl:this.remoteUrl}).then((function(t){console.log(t);var o=t.data;e.remoteOptions=o}))},methods:{}},u=i,s=o("2877"),c=Object(s["a"])(u,l,n,!1,null,"63422609",null);t["default"]=c.exports},"8f73":function(e,t,o){"use strict";t["a"]={props:{mode:{type:String,default:"DESIGN"},required:{type:Boolean,default:!1}},data:function(){return{}},computed:{_value:{get:function(){return this.value},set:function(e){this.$emit("input",e)}}}}},c7ea:function(e,t,o){"use strict";o.d(t,"a",(function(){return n}));var l=o("0c6d");function n(e){return Object(l["a"])({url:"/form/selectOptions",method:"post",data:e})}}}]);
//# sourceMappingURL=chunk-3c48e96a.2f8a1a4c.js.map