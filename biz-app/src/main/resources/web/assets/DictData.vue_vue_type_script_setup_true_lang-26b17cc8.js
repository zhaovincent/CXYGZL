import{aO as d,d as ae,L as le,r as v,K as F,o as ne,ca as ie,a as p,c as se,k as z,a5 as e,w as o,f as a,a3 as r,P as k,l as f,ae as re,as as ue,am as de,q as pe,a7 as N,an as me,aA as ce,aB as _e}from"./index-e01dade3.js";import{v as fe}from"./el-loading-b076b1a7.js";import{E as ge}from"./el-dialog-1471a1af.js";import{E as ye,a as ve}from"./el-radio-70657465.js";import{E as be}from"./el-input-number-182b17d2.js";import{E as Ce}from"./el-card-16bdf0d8.js";import{_ as Ve}from"./index-1cadfa40.js";import{E as ke,a as he}from"./el-table-column-7cf019e1.js";import"./el-checkbox-123bf6dd.js";import"./el-tooltip-4ed993c7.js";import"./el-popper-ee5283d6.js";import"./el-scrollbar-f2029ad1.js";import{_ as De}from"./edit-26548300.js";import{E as Ee}from"./el-tag-929563f7.js";import{_ as we,a as xe,b as Fe}from"./search-93a6394a.js";import{_ as Ne}from"./plus-95522b44.js";/* empty css                */import{E as U,a as Te}from"./el-form-item-85be6a3a.js";function at(n){return d({url:"/api/v1/dict/types/page",method:"get",params:n})}function lt(n){return d({url:"/api/v1/dict/types/"+n+"/form",method:"get"})}function nt(n){return d({url:"/api/v1/dict/types",method:"post",data:n})}function it(n,m){return d({url:"/api/v1/dict/types/"+n,method:"put",data:m})}function st(n){return d({url:"/api/v1/dict/types/"+n,method:"delete"})}function Ue(n){return d({url:"/api/v1/dict/page",method:"get",params:n})}function Be(n){return d({url:"/api/v1/dict/"+n+"/form",method:"get"})}function Se(n){return d({url:"/api/v1/dict",method:"post",data:n})}function Pe(n,m){return d({url:"/api/v1/dict/"+n,method:"put",data:m})}function Re(n){return d({url:"/api/v1/dict/"+n,method:"delete"})}const qe={class:"app-container"},Ie={class:"search"},Me={class:"dialog-footer"},rt=ae({name:"DictData",inheritAttrs:!1,__name:"DictData",props:{typeCode:{type:String,default:()=>""},typeName:{type:String,default:()=>""}},setup(n){const m=n;le(()=>m.typeCode,s=>{u.typeCode=s,C()});const B=v(U),h=v(U),y=v(!1),T=v([]),b=v(0),u=F({pageNum:1,pageSize:10,typeCode:m.typeCode}),S=v(),g=F({visible:!1}),i=F({status:1,typeCode:m.typeCode,sort:1}),L=F({name:[{required:!0,message:"请输入字典名称",trigger:"blur"}],value:[{required:!0,message:"请输入字典值",trigger:"blur"}]});function D(){u.typeCode&&(y.value=!0,Ue(u).then(({data:s})=>{S.value=s.list,b.value=s.total}).finally(()=>y.value=!1))}function C(){B.value.resetFields(),u.pageNum=1,D()}function j(s){T.value=s.map(t=>t.id)}function P(s){g.visible=!0,s?(g.title="修改字典",Be(s).then(({data:t})=>{Object.assign(i,t)})):g.title="新增字典"}function A(){y.value=!1,h.value.validate(s=>{if(s){const t=i.id;t?Pe(t,i).then(()=>{N.success("修改成功"),E(),C()}).finally(()=>y.value=!1):Se(i).then(()=>{N.success("新增成功"),E(),C()}).finally(()=>y.value=!1)}})}function E(){g.visible=!1,O()}function O(){h.value.resetFields(),h.value.clearValidate(),i.id=void 0,i.status=1,i.sort=1,i.typeCode=m.typeCode}function R(s){const t=[s||T.value].join(",");if(!t){N.warning("请勾选删除项");return}me.confirm("确认删除已选中的数据项?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(()=>{Re(t).then(()=>{N.success("删除成功"),C()})})}return ne(()=>{D()}),(s,t)=>{const w=ce,c=Te,Q=we,_=_e,G=xe,q=U,K=Ne,I=Fe,V=ke,M=Ee,H=De,J=he,W=Ve,X=Ce,Y=be,$=ye,Z=ve,ee=ge,x=ie("hasPerm"),te=fe;return p(),se("div",qe,[z("div",Ie,[e(q,{ref_key:"queryFormRef",ref:B,model:a(u),inline:!0},{default:o(()=>[e(c,{label:"关键字",prop:"name"},{default:o(()=>[e(w,{modelValue:a(u).name,"onUpdate:modelValue":t[0]||(t[0]=l=>a(u).name=l),placeholder:"字典名称",clearable:""},null,8,["modelValue"])]),_:1}),e(c,null,{default:o(()=>[e(_,{type:"primary",onClick:D},{default:o(()=>[e(Q),r("搜索")]),_:1}),e(_,{onClick:C},{default:o(()=>[e(G),r("重置")]),_:1})]),_:1})]),_:1},8,["model"])]),e(X,{shadow:"never"},{header:o(()=>[k((p(),f(_,{type:"success",onClick:t[1]||(t[1]=l=>P())},{default:o(()=>[e(K),r("新增")]),_:1})),[[x,["sys:dict:add"]]]),k((p(),f(_,{type:"danger",disabled:a(T).length===0,onClick:t[2]||(t[2]=l=>R())},{default:o(()=>[e(I),r("删除")]),_:1},8,["disabled"])),[[x,["sys:dict:delete"]]])]),default:o(()=>[k((p(),f(J,{data:a(S),border:"",onSelectionChange:j},{default:o(()=>[e(V,{type:"selection",width:"50"}),e(V,{label:"字典名称",prop:"name"}),e(V,{label:"字典值",prop:"value"}),e(V,{label:"状态",align:"center"},{default:o(l=>[l.row.status===1?(p(),f(M,{key:0,type:"success"},{default:o(()=>[r("启用")]),_:1})):(p(),f(M,{key:1,type:"info"},{default:o(()=>[r("禁用")]),_:1}))]),_:1}),e(V,{fixed:"right",label:"操作",align:"center"},{default:o(l=>[k((p(),f(_,{type:"primary",link:"",onClick:oe=>P(l.row.id)},{default:o(()=>[e(H),r("编辑")]),_:2},1032,["onClick"])),[[x,["sys:dict:edit"]]]),k((p(),f(_,{type:"primary",link:"",onClick:re(oe=>R(l.row.id),["stop"])},{default:o(()=>[e(I),r("删除")]),_:2},1032,["onClick"])),[[x,["sys:dict:delete"]]])]),_:1})]),_:1},8,["data"])),[[te,a(y)]]),a(b)>0?(p(),f(W,{key:0,total:a(b),"onUpdate:total":t[3]||(t[3]=l=>ue(b)?b.value=l:null),page:a(u).pageNum,"onUpdate:page":t[4]||(t[4]=l=>a(u).pageNum=l),limit:a(u).pageSize,"onUpdate:limit":t[5]||(t[5]=l=>a(u).pageSize=l),onPagination:D},null,8,["total","page","limit"])):de("",!0)]),_:1}),e(ee,{modelValue:a(g).visible,"onUpdate:modelValue":t[11]||(t[11]=l=>a(g).visible=l),title:a(g).title,width:"500px",onClose:E},{footer:o(()=>[z("div",Me,[e(_,{type:"primary",onClick:A},{default:o(()=>[r("确 定")]),_:1}),e(_,{onClick:E},{default:o(()=>[r("取 消")]),_:1})])]),default:o(()=>[e(q,{ref_key:"dataFormRef",ref:h,model:a(i),rules:a(L),"label-width":"100px"},{default:o(()=>[e(c,{label:"字典名称"},{default:o(()=>[r(pe(n.typeName),1)]),_:1}),e(c,{label:"字典名称",prop:"name"},{default:o(()=>[e(w,{modelValue:a(i).name,"onUpdate:modelValue":t[6]||(t[6]=l=>a(i).name=l),placeholder:"请输入字典名称"},null,8,["modelValue"])]),_:1}),e(c,{label:"字典值",prop:"value"},{default:o(()=>[e(w,{modelValue:a(i).value,"onUpdate:modelValue":t[7]||(t[7]=l=>a(i).value=l),placeholder:"字典值"},null,8,["modelValue"])]),_:1}),e(c,{label:"排序",prop:"sort"},{default:o(()=>[e(Y,{modelValue:a(i).sort,"onUpdate:modelValue":t[8]||(t[8]=l=>a(i).sort=l),"controls-position":"right",min:0},null,8,["modelValue"])]),_:1}),e(c,{label:"状态",prop:"status"},{default:o(()=>[e(Z,{modelValue:a(i).status,"onUpdate:modelValue":t[9]||(t[9]=l=>a(i).status=l)},{default:o(()=>[e($,{label:1},{default:o(()=>[r("正常")]),_:1}),e($,{label:0},{default:o(()=>[r("停用")]),_:1})]),_:1},8,["modelValue"])]),_:1}),e(c,{label:"备注",prop:"remark"},{default:o(()=>[e(w,{modelValue:a(i).remark,"onUpdate:modelValue":t[10]||(t[10]=l=>a(i).remark=l),type:"textarea"},null,8,["modelValue"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue","title"])])}}});export{rt as _,lt as a,nt as b,st as d,at as g,it as u};
