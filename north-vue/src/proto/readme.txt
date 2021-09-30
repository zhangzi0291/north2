生成对应的js文件和ts描述
pbjs -t static-module -w commonjs -o Protobuf.js Protobuf.proto
pbts -o Protobuf.d.ts Protobuf.js

调用
import ProtobufProto from "@/proto/Protobuf"

var Protobuf = ProtobufProto.Protobuf.decode(buf)
Protobuf.title
Protobuf.message
