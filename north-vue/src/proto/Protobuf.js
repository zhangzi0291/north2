/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
"use strict";

var $protobuf = require("protobufjs/minimal");

// Common aliases
var $Reader = $protobuf.Reader, $Writer = $protobuf.Writer, $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots["default"] || ($protobuf.roots["default"] = {});

$root.HomeInfo = (function () {

    /**
     * Properties of a HomeInfo.
     * @exports IHomeInfo
     * @interface IHomeInfo
     * @property {string|null} [id] HomeInfo id
     * @property {number|null} [totalUser] HomeInfo totalUser
     * @property {number|null} [onlineUser] HomeInfo onlineUser
     * @property {number|null} [todayUser] HomeInfo todayUser
     * @property {HomeInfo.ICpuInfo|null} [cpu] HomeInfo cpu
     * @property {HomeInfo.IMemeryInfo|null} [memery] HomeInfo memery
     * @property {Array.<HomeInfo.IDiskInfo>|null} [disk] HomeInfo disk
     */

    /**
     * Constructs a new HomeInfo.
     * @exports HomeInfo
     * @classdesc Represents a HomeInfo.
     * @implements IHomeInfo
     * @constructor
     * @param {IHomeInfo=} [properties] Properties to set
     */
    function HomeInfo(properties) {
        this.disk = [];
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * HomeInfo id.
     * @member {string} id
     * @memberof HomeInfo
     * @instance
     */
    HomeInfo.prototype.id = "";

    /**
     * HomeInfo totalUser.
     * @member {number} totalUser
     * @memberof HomeInfo
     * @instance
     */
    HomeInfo.prototype.totalUser = 0;

    /**
     * HomeInfo onlineUser.
     * @member {number} onlineUser
     * @memberof HomeInfo
     * @instance
     */
    HomeInfo.prototype.onlineUser = 0;

    /**
     * HomeInfo todayUser.
     * @member {number} todayUser
     * @memberof HomeInfo
     * @instance
     */
    HomeInfo.prototype.todayUser = 0;

    /**
     * HomeInfo cpu.
     * @member {HomeInfo.ICpuInfo|null|undefined} cpu
     * @memberof HomeInfo
     * @instance
     */
    HomeInfo.prototype.cpu = null;

    /**
     * HomeInfo memery.
     * @member {HomeInfo.IMemeryInfo|null|undefined} memery
     * @memberof HomeInfo
     * @instance
     */
    HomeInfo.prototype.memery = null;

    /**
     * HomeInfo disk.
     * @member {Array.<HomeInfo.IDiskInfo>} disk
     * @memberof HomeInfo
     * @instance
     */
    HomeInfo.prototype.disk = $util.emptyArray;

    /**
     * Creates a new HomeInfo instance using the specified properties.
     * @function create
     * @memberof HomeInfo
     * @static
     * @param {IHomeInfo=} [properties] Properties to set
     * @returns {HomeInfo} HomeInfo instance
     */
    HomeInfo.create = function create(properties) {
        return new HomeInfo(properties);
    };

    /**
     * Encodes the specified HomeInfo message. Does not implicitly {@link HomeInfo.verify|verify} messages.
     * @function encode
     * @memberof HomeInfo
     * @static
     * @param {IHomeInfo} message HomeInfo message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    HomeInfo.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.id != null && Object.hasOwnProperty.call(message, "id"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.id);
        if (message.totalUser != null && Object.hasOwnProperty.call(message, "totalUser"))
            writer.uint32(/* id 21, wireType 0 =*/168).int32(message.totalUser);
        if (message.onlineUser != null && Object.hasOwnProperty.call(message, "onlineUser"))
            writer.uint32(/* id 22, wireType 0 =*/176).int32(message.onlineUser);
        if (message.todayUser != null && Object.hasOwnProperty.call(message, "todayUser"))
            writer.uint32(/* id 23, wireType 0 =*/184).int32(message.todayUser);
        if (message.cpu != null && Object.hasOwnProperty.call(message, "cpu"))
            $root.HomeInfo.CpuInfo.encode(message.cpu, writer.uint32(/* id 24, wireType 2 =*/194).fork()).ldelim();
        if (message.memery != null && Object.hasOwnProperty.call(message, "memery"))
            $root.HomeInfo.MemeryInfo.encode(message.memery, writer.uint32(/* id 25, wireType 2 =*/202).fork()).ldelim();
        if (message.disk != null && message.disk.length)
            for (var i = 0; i < message.disk.length; ++i)
                $root.HomeInfo.DiskInfo.encode(message.disk[i], writer.uint32(/* id 26, wireType 2 =*/210).fork()).ldelim();
        return writer;
    };

    /**
     * Encodes the specified HomeInfo message, length delimited. Does not implicitly {@link HomeInfo.verify|verify} messages.
     * @function encodeDelimited
     * @memberof HomeInfo
     * @static
     * @param {IHomeInfo} message HomeInfo message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    HomeInfo.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a HomeInfo message from the specified reader or buffer.
     * @function decode
     * @memberof HomeInfo
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {HomeInfo} HomeInfo
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    HomeInfo.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.HomeInfo();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
                case 1:
                    message.id = reader.string();
                    break;
                case 21:
                    message.totalUser = reader.int32();
                    break;
                case 22:
                    message.onlineUser = reader.int32();
                    break;
                case 23:
                    message.todayUser = reader.int32();
                    break;
                case 24:
                    message.cpu = $root.HomeInfo.CpuInfo.decode(reader, reader.uint32());
                    break;
                case 25:
                    message.memery = $root.HomeInfo.MemeryInfo.decode(reader, reader.uint32());
                    break;
                case 26:
                    if (!(message.disk && message.disk.length))
                        message.disk = [];
                    message.disk.push($root.HomeInfo.DiskInfo.decode(reader, reader.uint32()));
                    break;
                default:
                    reader.skipType(tag & 7);
                    break;
            }
        }
        return message;
    };

    /**
     * Decodes a HomeInfo message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof HomeInfo
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {HomeInfo} HomeInfo
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    HomeInfo.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a HomeInfo message.
     * @function verify
     * @memberof HomeInfo
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    HomeInfo.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.id != null && message.hasOwnProperty("id"))
            if (!$util.isString(message.id))
                return "id: string expected";
        if (message.totalUser != null && message.hasOwnProperty("totalUser"))
            if (!$util.isInteger(message.totalUser))
                return "totalUser: integer expected";
        if (message.onlineUser != null && message.hasOwnProperty("onlineUser"))
            if (!$util.isInteger(message.onlineUser))
                return "onlineUser: integer expected";
        if (message.todayUser != null && message.hasOwnProperty("todayUser"))
            if (!$util.isInteger(message.todayUser))
                return "todayUser: integer expected";
        if (message.cpu != null && message.hasOwnProperty("cpu")) {
            var error = $root.HomeInfo.CpuInfo.verify(message.cpu);
            if (error)
                return "cpu." + error;
        }
        if (message.memery != null && message.hasOwnProperty("memery")) {
            var error = $root.HomeInfo.MemeryInfo.verify(message.memery);
            if (error)
                return "memery." + error;
        }
        if (message.disk != null && message.hasOwnProperty("disk")) {
            if (!Array.isArray(message.disk))
                return "disk: array expected";
            for (var i = 0; i < message.disk.length; ++i) {
                var error = $root.HomeInfo.DiskInfo.verify(message.disk[i]);
                if (error)
                    return "disk." + error;
            }
        }
        return null;
    };

    /**
     * Creates a HomeInfo message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof HomeInfo
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {HomeInfo} HomeInfo
     */
    HomeInfo.fromObject = function fromObject(object) {
        if (object instanceof $root.HomeInfo)
            return object;
        var message = new $root.HomeInfo();
        if (object.id != null)
            message.id = String(object.id);
        if (object.totalUser != null)
            message.totalUser = object.totalUser | 0;
        if (object.onlineUser != null)
            message.onlineUser = object.onlineUser | 0;
        if (object.todayUser != null)
            message.todayUser = object.todayUser | 0;
        if (object.cpu != null) {
            if (typeof object.cpu !== "object")
                throw TypeError(".HomeInfo.cpu: object expected");
            message.cpu = $root.HomeInfo.CpuInfo.fromObject(object.cpu);
        }
        if (object.memery != null) {
            if (typeof object.memery !== "object")
                throw TypeError(".HomeInfo.memery: object expected");
            message.memery = $root.HomeInfo.MemeryInfo.fromObject(object.memery);
        }
        if (object.disk) {
            if (!Array.isArray(object.disk))
                throw TypeError(".HomeInfo.disk: array expected");
            message.disk = [];
            for (var i = 0; i < object.disk.length; ++i) {
                if (typeof object.disk[i] !== "object")
                    throw TypeError(".HomeInfo.disk: object expected");
                message.disk[i] = $root.HomeInfo.DiskInfo.fromObject(object.disk[i]);
            }
        }
        return message;
    };

    /**
     * Creates a plain object from a HomeInfo message. Also converts values to other types if specified.
     * @function toObject
     * @memberof HomeInfo
     * @static
     * @param {HomeInfo} message HomeInfo
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    HomeInfo.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.arrays || options.defaults)
            object.disk = [];
        if (options.defaults) {
            object.id = "";
            object.totalUser = 0;
            object.onlineUser = 0;
            object.todayUser = 0;
            object.cpu = null;
            object.memery = null;
        }
        if (message.id != null && message.hasOwnProperty("id"))
            object.id = message.id;
        if (message.totalUser != null && message.hasOwnProperty("totalUser"))
            object.totalUser = message.totalUser;
        if (message.onlineUser != null && message.hasOwnProperty("onlineUser"))
            object.onlineUser = message.onlineUser;
        if (message.todayUser != null && message.hasOwnProperty("todayUser"))
            object.todayUser = message.todayUser;
        if (message.cpu != null && message.hasOwnProperty("cpu"))
            object.cpu = $root.HomeInfo.CpuInfo.toObject(message.cpu, options);
        if (message.memery != null && message.hasOwnProperty("memery"))
            object.memery = $root.HomeInfo.MemeryInfo.toObject(message.memery, options);
        if (message.disk && message.disk.length) {
            object.disk = [];
            for (var j = 0; j < message.disk.length; ++j)
                object.disk[j] = $root.HomeInfo.DiskInfo.toObject(message.disk[j], options);
        }
        return object;
    };

    /**
     * Converts this HomeInfo to JSON.
     * @function toJSON
     * @memberof HomeInfo
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    HomeInfo.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    HomeInfo.CpuInfo = (function () {

        /**
         * Properties of a CpuInfo.
         * @memberof HomeInfo
         * @interface ICpuInfo
         * @property {string|null} [system] CpuInfo system
         * @property {string|null} [process] CpuInfo process
         * @property {string|null} [loadAverage] CpuInfo loadAverage
         */

        /**
         * Constructs a new CpuInfo.
         * @memberof HomeInfo
         * @classdesc Represents a CpuInfo.
         * @implements ICpuInfo
         * @constructor
         * @param {HomeInfo.ICpuInfo=} [properties] Properties to set
         */
        function CpuInfo(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null)
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * CpuInfo system.
         * @member {string} system
         * @memberof HomeInfo.CpuInfo
         * @instance
         */
        CpuInfo.prototype.system = "";

        /**
         * CpuInfo process.
         * @member {string} process
         * @memberof HomeInfo.CpuInfo
         * @instance
         */
        CpuInfo.prototype.process = "";

        /**
         * CpuInfo loadAverage.
         * @member {string} loadAverage
         * @memberof HomeInfo.CpuInfo
         * @instance
         */
        CpuInfo.prototype.loadAverage = "";

        /**
         * Creates a new CpuInfo instance using the specified properties.
         * @function create
         * @memberof HomeInfo.CpuInfo
         * @static
         * @param {HomeInfo.ICpuInfo=} [properties] Properties to set
         * @returns {HomeInfo.CpuInfo} CpuInfo instance
         */
        CpuInfo.create = function create(properties) {
            return new CpuInfo(properties);
        };

        /**
         * Encodes the specified CpuInfo message. Does not implicitly {@link HomeInfo.CpuInfo.verify|verify} messages.
         * @function encode
         * @memberof HomeInfo.CpuInfo
         * @static
         * @param {HomeInfo.ICpuInfo} message CpuInfo message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        CpuInfo.encode = function encode(message, writer) {
            if (!writer)
                writer = $Writer.create();
            if (message.system != null && Object.hasOwnProperty.call(message, "system"))
                writer.uint32(/* id 11, wireType 2 =*/90).string(message.system);
            if (message.process != null && Object.hasOwnProperty.call(message, "process"))
                writer.uint32(/* id 12, wireType 2 =*/98).string(message.process);
            if (message.loadAverage != null && Object.hasOwnProperty.call(message, "loadAverage"))
                writer.uint32(/* id 13, wireType 2 =*/106).string(message.loadAverage);
            return writer;
        };

        /**
         * Encodes the specified CpuInfo message, length delimited. Does not implicitly {@link HomeInfo.CpuInfo.verify|verify} messages.
         * @function encodeDelimited
         * @memberof HomeInfo.CpuInfo
         * @static
         * @param {HomeInfo.ICpuInfo} message CpuInfo message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        CpuInfo.encodeDelimited = function encodeDelimited(message, writer) {
            return this.encode(message, writer).ldelim();
        };

        /**
         * Decodes a CpuInfo message from the specified reader or buffer.
         * @function decode
         * @memberof HomeInfo.CpuInfo
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {HomeInfo.CpuInfo} CpuInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        CpuInfo.decode = function decode(reader, length) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.HomeInfo.CpuInfo();
            while (reader.pos < end) {
                var tag = reader.uint32();
                switch (tag >>> 3) {
                    case 11:
                        message.system = reader.string();
                        break;
                    case 12:
                        message.process = reader.string();
                        break;
                    case 13:
                        message.loadAverage = reader.string();
                        break;
                    default:
                        reader.skipType(tag & 7);
                        break;
                }
            }
            return message;
        };

        /**
         * Decodes a CpuInfo message from the specified reader or buffer, length delimited.
         * @function decodeDelimited
         * @memberof HomeInfo.CpuInfo
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @returns {HomeInfo.CpuInfo} CpuInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        CpuInfo.decodeDelimited = function decodeDelimited(reader) {
            if (!(reader instanceof $Reader))
                reader = new $Reader(reader);
            return this.decode(reader, reader.uint32());
        };

        /**
         * Verifies a CpuInfo message.
         * @function verify
         * @memberof HomeInfo.CpuInfo
         * @static
         * @param {Object.<string,*>} message Plain object to verify
         * @returns {string|null} `null` if valid, otherwise the reason why it is not
         */
        CpuInfo.verify = function verify(message) {
            if (typeof message !== "object" || message === null)
                return "object expected";
            if (message.system != null && message.hasOwnProperty("system"))
                if (!$util.isString(message.system))
                    return "system: string expected";
            if (message.process != null && message.hasOwnProperty("process"))
                if (!$util.isString(message.process))
                    return "process: string expected";
            if (message.loadAverage != null && message.hasOwnProperty("loadAverage"))
                if (!$util.isString(message.loadAverage))
                    return "loadAverage: string expected";
            return null;
        };

        /**
         * Creates a CpuInfo message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof HomeInfo.CpuInfo
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {HomeInfo.CpuInfo} CpuInfo
         */
        CpuInfo.fromObject = function fromObject(object) {
            if (object instanceof $root.HomeInfo.CpuInfo)
                return object;
            var message = new $root.HomeInfo.CpuInfo();
            if (object.system != null)
                message.system = String(object.system);
            if (object.process != null)
                message.process = String(object.process);
            if (object.loadAverage != null)
                message.loadAverage = String(object.loadAverage);
            return message;
        };

        /**
         * Creates a plain object from a CpuInfo message. Also converts values to other types if specified.
         * @function toObject
         * @memberof HomeInfo.CpuInfo
         * @static
         * @param {HomeInfo.CpuInfo} message CpuInfo
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        CpuInfo.toObject = function toObject(message, options) {
            if (!options)
                options = {};
            var object = {};
            if (options.defaults) {
                object.system = "";
                object.process = "";
                object.loadAverage = "";
            }
            if (message.system != null && message.hasOwnProperty("system"))
                object.system = message.system;
            if (message.process != null && message.hasOwnProperty("process"))
                object.process = message.process;
            if (message.loadAverage != null && message.hasOwnProperty("loadAverage"))
                object.loadAverage = message.loadAverage;
            return object;
        };

        /**
         * Converts this CpuInfo to JSON.
         * @function toJSON
         * @memberof HomeInfo.CpuInfo
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        CpuInfo.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        return CpuInfo;
    })();

    HomeInfo.MemeryInfo = (function () {

        /**
         * Properties of a MemeryInfo.
         * @memberof HomeInfo
         * @interface IMemeryInfo
         * @property {string|null} [used] MemeryInfo used
         * @property {string|null} [total] MemeryInfo total
         */

        /**
         * Constructs a new MemeryInfo.
         * @memberof HomeInfo
         * @classdesc Represents a MemeryInfo.
         * @implements IMemeryInfo
         * @constructor
         * @param {HomeInfo.IMemeryInfo=} [properties] Properties to set
         */
        function MemeryInfo(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null)
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * MemeryInfo used.
         * @member {string} used
         * @memberof HomeInfo.MemeryInfo
         * @instance
         */
        MemeryInfo.prototype.used = "";

        /**
         * MemeryInfo total.
         * @member {string} total
         * @memberof HomeInfo.MemeryInfo
         * @instance
         */
        MemeryInfo.prototype.total = "";

        /**
         * Creates a new MemeryInfo instance using the specified properties.
         * @function create
         * @memberof HomeInfo.MemeryInfo
         * @static
         * @param {HomeInfo.IMemeryInfo=} [properties] Properties to set
         * @returns {HomeInfo.MemeryInfo} MemeryInfo instance
         */
        MemeryInfo.create = function create(properties) {
            return new MemeryInfo(properties);
        };

        /**
         * Encodes the specified MemeryInfo message. Does not implicitly {@link HomeInfo.MemeryInfo.verify|verify} messages.
         * @function encode
         * @memberof HomeInfo.MemeryInfo
         * @static
         * @param {HomeInfo.IMemeryInfo} message MemeryInfo message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        MemeryInfo.encode = function encode(message, writer) {
            if (!writer)
                writer = $Writer.create();
            if (message.used != null && Object.hasOwnProperty.call(message, "used"))
                writer.uint32(/* id 11, wireType 2 =*/90).string(message.used);
            if (message.total != null && Object.hasOwnProperty.call(message, "total"))
                writer.uint32(/* id 12, wireType 2 =*/98).string(message.total);
            return writer;
        };

        /**
         * Encodes the specified MemeryInfo message, length delimited. Does not implicitly {@link HomeInfo.MemeryInfo.verify|verify} messages.
         * @function encodeDelimited
         * @memberof HomeInfo.MemeryInfo
         * @static
         * @param {HomeInfo.IMemeryInfo} message MemeryInfo message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        MemeryInfo.encodeDelimited = function encodeDelimited(message, writer) {
            return this.encode(message, writer).ldelim();
        };

        /**
         * Decodes a MemeryInfo message from the specified reader or buffer.
         * @function decode
         * @memberof HomeInfo.MemeryInfo
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {HomeInfo.MemeryInfo} MemeryInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        MemeryInfo.decode = function decode(reader, length) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            var end = length === undefined ? reader.len : reader.pos + length,
                message = new $root.HomeInfo.MemeryInfo();
            while (reader.pos < end) {
                var tag = reader.uint32();
                switch (tag >>> 3) {
                    case 11:
                        message.used = reader.string();
                        break;
                    case 12:
                        message.total = reader.string();
                        break;
                    default:
                        reader.skipType(tag & 7);
                        break;
                }
            }
            return message;
        };

        /**
         * Decodes a MemeryInfo message from the specified reader or buffer, length delimited.
         * @function decodeDelimited
         * @memberof HomeInfo.MemeryInfo
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @returns {HomeInfo.MemeryInfo} MemeryInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        MemeryInfo.decodeDelimited = function decodeDelimited(reader) {
            if (!(reader instanceof $Reader))
                reader = new $Reader(reader);
            return this.decode(reader, reader.uint32());
        };

        /**
         * Verifies a MemeryInfo message.
         * @function verify
         * @memberof HomeInfo.MemeryInfo
         * @static
         * @param {Object.<string,*>} message Plain object to verify
         * @returns {string|null} `null` if valid, otherwise the reason why it is not
         */
        MemeryInfo.verify = function verify(message) {
            if (typeof message !== "object" || message === null)
                return "object expected";
            if (message.used != null && message.hasOwnProperty("used"))
                if (!$util.isString(message.used))
                    return "used: string expected";
            if (message.total != null && message.hasOwnProperty("total"))
                if (!$util.isString(message.total))
                    return "total: string expected";
            return null;
        };

        /**
         * Creates a MemeryInfo message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof HomeInfo.MemeryInfo
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {HomeInfo.MemeryInfo} MemeryInfo
         */
        MemeryInfo.fromObject = function fromObject(object) {
            if (object instanceof $root.HomeInfo.MemeryInfo)
                return object;
            var message = new $root.HomeInfo.MemeryInfo();
            if (object.used != null)
                message.used = String(object.used);
            if (object.total != null)
                message.total = String(object.total);
            return message;
        };

        /**
         * Creates a plain object from a MemeryInfo message. Also converts values to other types if specified.
         * @function toObject
         * @memberof HomeInfo.MemeryInfo
         * @static
         * @param {HomeInfo.MemeryInfo} message MemeryInfo
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        MemeryInfo.toObject = function toObject(message, options) {
            if (!options)
                options = {};
            var object = {};
            if (options.defaults) {
                object.used = "";
                object.total = "";
            }
            if (message.used != null && message.hasOwnProperty("used"))
                object.used = message.used;
            if (message.total != null && message.hasOwnProperty("total"))
                object.total = message.total;
            return object;
        };

        /**
         * Converts this MemeryInfo to JSON.
         * @function toJSON
         * @memberof HomeInfo.MemeryInfo
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        MemeryInfo.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        return MemeryInfo;
    })();

    HomeInfo.DiskInfo = (function () {

        /**
         * Properties of a DiskInfo.
         * @memberof HomeInfo
         * @interface IDiskInfo
         * @property {string|null} [used] DiskInfo used
         * @property {string|null} [total] DiskInfo total
         * @property {string|null} [name] DiskInfo name
         */

        /**
         * Constructs a new DiskInfo.
         * @memberof HomeInfo
         * @classdesc Represents a DiskInfo.
         * @implements IDiskInfo
         * @constructor
         * @param {HomeInfo.IDiskInfo=} [properties] Properties to set
         */
        function DiskInfo(properties) {
            if (properties)
                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                    if (properties[keys[i]] != null)
                        this[keys[i]] = properties[keys[i]];
        }

        /**
         * DiskInfo used.
         * @member {string} used
         * @memberof HomeInfo.DiskInfo
         * @instance
         */
        DiskInfo.prototype.used = "";

        /**
         * DiskInfo total.
         * @member {string} total
         * @memberof HomeInfo.DiskInfo
         * @instance
         */
        DiskInfo.prototype.total = "";

        /**
         * DiskInfo name.
         * @member {string} name
         * @memberof HomeInfo.DiskInfo
         * @instance
         */
        DiskInfo.prototype.name = "";

        /**
         * Creates a new DiskInfo instance using the specified properties.
         * @function create
         * @memberof HomeInfo.DiskInfo
         * @static
         * @param {HomeInfo.IDiskInfo=} [properties] Properties to set
         * @returns {HomeInfo.DiskInfo} DiskInfo instance
         */
        DiskInfo.create = function create(properties) {
            return new DiskInfo(properties);
        };

        /**
         * Encodes the specified DiskInfo message. Does not implicitly {@link HomeInfo.DiskInfo.verify|verify} messages.
         * @function encode
         * @memberof HomeInfo.DiskInfo
         * @static
         * @param {HomeInfo.IDiskInfo} message DiskInfo message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        DiskInfo.encode = function encode(message, writer) {
            if (!writer)
                writer = $Writer.create();
            if (message.used != null && Object.hasOwnProperty.call(message, "used"))
                writer.uint32(/* id 11, wireType 2 =*/90).string(message.used);
            if (message.total != null && Object.hasOwnProperty.call(message, "total"))
                writer.uint32(/* id 12, wireType 2 =*/98).string(message.total);
            if (message.name != null && Object.hasOwnProperty.call(message, "name"))
                writer.uint32(/* id 13, wireType 2 =*/106).string(message.name);
            return writer;
        };

        /**
         * Encodes the specified DiskInfo message, length delimited. Does not implicitly {@link HomeInfo.DiskInfo.verify|verify} messages.
         * @function encodeDelimited
         * @memberof HomeInfo.DiskInfo
         * @static
         * @param {HomeInfo.IDiskInfo} message DiskInfo message or plain object to encode
         * @param {$protobuf.Writer} [writer] Writer to encode to
         * @returns {$protobuf.Writer} Writer
         */
        DiskInfo.encodeDelimited = function encodeDelimited(message, writer) {
            return this.encode(message, writer).ldelim();
        };

        /**
         * Decodes a DiskInfo message from the specified reader or buffer.
         * @function decode
         * @memberof HomeInfo.DiskInfo
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @param {number} [length] Message length if known beforehand
         * @returns {HomeInfo.DiskInfo} DiskInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        DiskInfo.decode = function decode(reader, length) {
            if (!(reader instanceof $Reader))
                reader = $Reader.create(reader);
            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.HomeInfo.DiskInfo();
            while (reader.pos < end) {
                var tag = reader.uint32();
                switch (tag >>> 3) {
                    case 11:
                        message.used = reader.string();
                        break;
                    case 12:
                        message.total = reader.string();
                        break;
                    case 13:
                        message.name = reader.string();
                        break;
                    default:
                        reader.skipType(tag & 7);
                        break;
                }
            }
            return message;
        };

        /**
         * Decodes a DiskInfo message from the specified reader or buffer, length delimited.
         * @function decodeDelimited
         * @memberof HomeInfo.DiskInfo
         * @static
         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
         * @returns {HomeInfo.DiskInfo} DiskInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        DiskInfo.decodeDelimited = function decodeDelimited(reader) {
            if (!(reader instanceof $Reader))
                reader = new $Reader(reader);
            return this.decode(reader, reader.uint32());
        };

        /**
         * Verifies a DiskInfo message.
         * @function verify
         * @memberof HomeInfo.DiskInfo
         * @static
         * @param {Object.<string,*>} message Plain object to verify
         * @returns {string|null} `null` if valid, otherwise the reason why it is not
         */
        DiskInfo.verify = function verify(message) {
            if (typeof message !== "object" || message === null)
                return "object expected";
            if (message.used != null && message.hasOwnProperty("used"))
                if (!$util.isString(message.used))
                    return "used: string expected";
            if (message.total != null && message.hasOwnProperty("total"))
                if (!$util.isString(message.total))
                    return "total: string expected";
            if (message.name != null && message.hasOwnProperty("name"))
                if (!$util.isString(message.name))
                    return "name: string expected";
            return null;
        };

        /**
         * Creates a DiskInfo message from a plain object. Also converts values to their respective internal types.
         * @function fromObject
         * @memberof HomeInfo.DiskInfo
         * @static
         * @param {Object.<string,*>} object Plain object
         * @returns {HomeInfo.DiskInfo} DiskInfo
         */
        DiskInfo.fromObject = function fromObject(object) {
            if (object instanceof $root.HomeInfo.DiskInfo)
                return object;
            var message = new $root.HomeInfo.DiskInfo();
            if (object.used != null)
                message.used = String(object.used);
            if (object.total != null)
                message.total = String(object.total);
            if (object.name != null)
                message.name = String(object.name);
            return message;
        };

        /**
         * Creates a plain object from a DiskInfo message. Also converts values to other types if specified.
         * @function toObject
         * @memberof HomeInfo.DiskInfo
         * @static
         * @param {HomeInfo.DiskInfo} message DiskInfo
         * @param {$protobuf.IConversionOptions} [options] Conversion options
         * @returns {Object.<string,*>} Plain object
         */
        DiskInfo.toObject = function toObject(message, options) {
            if (!options)
                options = {};
            var object = {};
            if (options.defaults) {
                object.used = "";
                object.total = "";
                object.name = "";
            }
            if (message.used != null && message.hasOwnProperty("used"))
                object.used = message.used;
            if (message.total != null && message.hasOwnProperty("total"))
                object.total = message.total;
            if (message.name != null && message.hasOwnProperty("name"))
                object.name = message.name;
            return object;
        };

        /**
         * Converts this DiskInfo to JSON.
         * @function toJSON
         * @memberof HomeInfo.DiskInfo
         * @instance
         * @returns {Object.<string,*>} JSON object
         */
        DiskInfo.prototype.toJSON = function toJSON() {
            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
        };

        return DiskInfo;
    })();

    return HomeInfo;
})();

$root.NotificationMessage = (function () {

    /**
     * Properties of a NotificationMessage.
     * @exports INotificationMessage
     * @interface INotificationMessage
     * @property {string|null} [id] NotificationMessage id
     * @property {string|null} [title] NotificationMessage title
     * @property {string|null} [message] NotificationMessage message
     */

    /**
     * Constructs a new NotificationMessage.
     * @exports NotificationMessage
     * @classdesc Represents a NotificationMessage.
     * @implements INotificationMessage
     * @constructor
     * @param {INotificationMessage=} [properties] Properties to set
     */
    function NotificationMessage(properties) {
        if (properties)
            for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                if (properties[keys[i]] != null)
                    this[keys[i]] = properties[keys[i]];
    }

    /**
     * NotificationMessage id.
     * @member {string} id
     * @memberof NotificationMessage
     * @instance
     */
    NotificationMessage.prototype.id = "";

    /**
     * NotificationMessage title.
     * @member {string} title
     * @memberof NotificationMessage
     * @instance
     */
    NotificationMessage.prototype.title = "";

    /**
     * NotificationMessage message.
     * @member {string} message
     * @memberof NotificationMessage
     * @instance
     */
    NotificationMessage.prototype.message = "";

    /**
     * Creates a new NotificationMessage instance using the specified properties.
     * @function create
     * @memberof NotificationMessage
     * @static
     * @param {INotificationMessage=} [properties] Properties to set
     * @returns {NotificationMessage} NotificationMessage instance
     */
    NotificationMessage.create = function create(properties) {
        return new NotificationMessage(properties);
    };

    /**
     * Encodes the specified NotificationMessage message. Does not implicitly {@link NotificationMessage.verify|verify} messages.
     * @function encode
     * @memberof NotificationMessage
     * @static
     * @param {INotificationMessage} message NotificationMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    NotificationMessage.encode = function encode(message, writer) {
        if (!writer)
            writer = $Writer.create();
        if (message.id != null && Object.hasOwnProperty.call(message, "id"))
            writer.uint32(/* id 1, wireType 2 =*/10).string(message.id);
        if (message.title != null && Object.hasOwnProperty.call(message, "title"))
            writer.uint32(/* id 20, wireType 2 =*/162).string(message.title);
        if (message.message != null && Object.hasOwnProperty.call(message, "message"))
            writer.uint32(/* id 21, wireType 2 =*/170).string(message.message);
        return writer;
    };

    /**
     * Encodes the specified NotificationMessage message, length delimited. Does not implicitly {@link NotificationMessage.verify|verify} messages.
     * @function encodeDelimited
     * @memberof NotificationMessage
     * @static
     * @param {INotificationMessage} message NotificationMessage message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    NotificationMessage.encodeDelimited = function encodeDelimited(message, writer) {
        return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a NotificationMessage message from the specified reader or buffer.
     * @function decode
     * @memberof NotificationMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {NotificationMessage} NotificationMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    NotificationMessage.decode = function decode(reader, length) {
        if (!(reader instanceof $Reader))
            reader = $Reader.create(reader);
        var end = length === undefined ? reader.len : reader.pos + length, message = new $root.NotificationMessage();
        while (reader.pos < end) {
            var tag = reader.uint32();
            switch (tag >>> 3) {
                case 1:
                    message.id = reader.string();
                    break;
                case 20:
                    message.title = reader.string();
                    break;
                case 21:
                    message.message = reader.string();
                    break;
                default:
                    reader.skipType(tag & 7);
                    break;
            }
        }
        return message;
    };

    /**
     * Decodes a NotificationMessage message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof NotificationMessage
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {NotificationMessage} NotificationMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    NotificationMessage.decodeDelimited = function decodeDelimited(reader) {
        if (!(reader instanceof $Reader))
            reader = new $Reader(reader);
        return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a NotificationMessage message.
     * @function verify
     * @memberof NotificationMessage
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    NotificationMessage.verify = function verify(message) {
        if (typeof message !== "object" || message === null)
            return "object expected";
        if (message.id != null && message.hasOwnProperty("id"))
            if (!$util.isString(message.id))
                return "id: string expected";
        if (message.title != null && message.hasOwnProperty("title"))
            if (!$util.isString(message.title))
                return "title: string expected";
        if (message.message != null && message.hasOwnProperty("message"))
            if (!$util.isString(message.message))
                return "message: string expected";
        return null;
    };

    /**
     * Creates a NotificationMessage message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof NotificationMessage
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {NotificationMessage} NotificationMessage
     */
    NotificationMessage.fromObject = function fromObject(object) {
        if (object instanceof $root.NotificationMessage)
            return object;
        var message = new $root.NotificationMessage();
        if (object.id != null)
            message.id = String(object.id);
        if (object.title != null)
            message.title = String(object.title);
        if (object.message != null)
            message.message = String(object.message);
        return message;
    };

    /**
     * Creates a plain object from a NotificationMessage message. Also converts values to other types if specified.
     * @function toObject
     * @memberof NotificationMessage
     * @static
     * @param {NotificationMessage} message NotificationMessage
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    NotificationMessage.toObject = function toObject(message, options) {
        if (!options)
            options = {};
        var object = {};
        if (options.defaults) {
            object.id = "";
            object.title = "";
            object.message = "";
        }
        if (message.id != null && message.hasOwnProperty("id"))
            object.id = message.id;
        if (message.title != null && message.hasOwnProperty("title"))
            object.title = message.title;
        if (message.message != null && message.hasOwnProperty("message"))
            object.message = message.message;
        return object;
    };

    /**
     * Converts this NotificationMessage to JSON.
     * @function toJSON
     * @memberof NotificationMessage
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    NotificationMessage.prototype.toJSON = function toJSON() {
        return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    return NotificationMessage;
})();

module.exports = $root;
