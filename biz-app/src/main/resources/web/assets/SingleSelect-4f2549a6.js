import{d as p,D as c,h as g,a,c as m,l as s,w as h,R as v,a6 as y,f as k,as as _}from"./index-e01dade3.js";import{a as b,E}from"./el-select-c154265f.js";import"./el-tag-929563f7.js";import"./el-scrollbar-f2029ad1.js";import"./el-popper-ee5283d6.js";const S=p({__name:"SingleSelect",props:{mode:{type:String,default:"D"},form:{type:Object,default:()=>{}}},setup(t,{expose:f}){const n=t;var i=c({get(){let e=n.form.props.value;return e&&e.length==1?e[0].key:void 0},set(e){let o=n.form.props.options.filter(l=>l.key===e);n.form.props.value=o}});return g(),f({getValidateRule:()=>{var e=n.form,o=(d,r,u)=>e.required&&(r==null||r.length==0)?u(new Error("请选择"+e.name)):(r==null,u());let l=[{validator:o,trigger:"change"}];return e.required&&l.push({required:!0,message:"请选择"+e.name,trigger:"change"}),l}}),(e,o)=>{const l=b,d=E;return a(),m("div",null,[t.mode==="D"?(a(),s(l,{key:0,style:{width:"100%"},disabled:!0,placeholder:t.form?t.form.placeholder:"",size:"large"},null,8,["placeholder"])):(a(),s(l,{key:1,style:{width:"100%"},modelValue:k(i),"onUpdate:modelValue":o[0]||(o[0]=r=>_(i)?i.value=r:i=r),disabled:t.form.perm==="R",placeholder:t.form.placeholder,size:"large"},{default:h(()=>[(a(!0),m(v,null,y(t.form.props.options,r=>(a(),s(d,{key:r.key,label:r.value,value:r.key},null,8,["label","value"]))),128))]),_:1},8,["modelValue","disabled","placeholder"]))])}}}),j=Object.freeze(Object.defineProperty({__proto__:null,default:S},Symbol.toStringTag,{value:"Module"}));export{j as _};