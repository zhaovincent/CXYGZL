import{d as A,h as D,a as p,c as v,l as O,R as b,a5 as h,w as E,a3 as I,q as j,am as B}from"./index-d5547550.js";import{E as L}from"./el-text-a7cd3acd.js";import{E as T}from"./el-input-number-f4eeab94.js";const U=A({__name:"Money",props:{mode:{type:String,default:"D"},form:{type:Object,default:()=>{}}},setup(t,{expose:y}){const C=t;function V(e){if(e==null||e===void 0)return"";var r=new Array("零","壹","贰","叁","肆","伍","陆","柒","捌","玖"),s=new Array("","拾","佰","仟"),o=new Array("","万","亿","兆"),c=new Array("角","分","毫","厘"),a="整",i="元",S=1e15,d,f,n="",g;if(e==""||(e=parseFloat(e),e>=S))return"";if(e==0)return n=r[0]+i+a,n;if(e=e.toString(),e.indexOf(".")==-1?(d=e,f=""):(g=e.split("."),d=g[0],f=g[1].substr(0,4)),parseInt(d,10)>0){let m=0,l=d.length;for(let u=0;u<l;u++){let x=d.substr(u,1),N=l-u-1,q=N/4,w=N%4;x=="0"?m++:(m>0&&(n+=r[0]),m=0,n+=r[parseInt(x)]+s[w]),w==0&&m<4&&(n+=o[q])}n+=i}if(f!=""){let m=f.length;for(let l=0;l<m;l++){let u=f.substr(l,1);u!="0"&&(n+=r[Number(u)]+c[l])}}return n==""?n+=r[0]+i+a:f==""&&(n+=a),n}const{proxy:R}=D();return y({getValidateRule:()=>{var e=C.form,r=e.props,s=(c,a,i)=>e.required&&!a?i(new Error("请填写"+e.name)):a?r.radixNum&&R.$getNumberRadixNum(a)>r.radixNum?i(new Error("小数位数不能大于"+r.radixNum)):r.min&&a<r.min?i(new Error("数值不能小于"+r.min)):r.max&&a>r.max?i(new Error("数值不能大于"+r.max)):i():i();let o=[{validator:s,trigger:"blur"}];return e.required&&o.push({required:!0,message:"请填写"+e.name,trigger:"blur"}),o}}),(e,r)=>{const s=T,o=L;return p(),v("div",null,[t.mode==="D"?(p(),O(s,{key:0,style:{width:"100%"},"controls-position":"right",disabled:!0,placeholder:t.form?t.form.placeholder:""},null,8,["placeholder"])):(p(),v(b,{key:1},[h(s,{style:{width:"100%"},"controls-position":"right",modelValue:t.form.props.value,"onUpdate:modelValue":r[0]||(r[0]=c=>t.form.props.value=c),precision:t.form.props.radixNum,disabled:t.form.perm==="R",placeholder:t.form.placeholder},null,8,["modelValue","precision","disabled","placeholder"]),t.form.props.showChinese?(p(),v(b,{key:0},[h(o,null,{default:E(()=>[I(" 大写： ")]),_:1}),h(o,{type:"info"},{default:E(()=>[I(j(V(t.form.props.value)),1)]),_:1})],64)):B("",!0)],64))])}}}),M=Object.freeze(Object.defineProperty({__proto__:null,default:U},Symbol.toStringTag,{value:"Module"}));export{M as _};