import{p as e,c as h}from"./_Uint8Array-aeb50f83.js";import{dg as m,d7 as t}from"./index-e01dade3.js";var r=m?m.isConcatSpreadable:void 0;function y(n){return t(n)||e(n)||!!(r&&n&&n[r])}function d(n,f,a,s,o){var b=-1,g=n.length;for(a||(a=y),o||(o=[]);++b<g;){var i=n[b];f>0&&a(i)?f>1?d(i,f-1,a,s,o):h(o,i):s||(o[o.length]=i)}return o}export{d as b};