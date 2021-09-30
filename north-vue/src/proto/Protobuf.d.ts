import * as $protobuf from "protobufjs";
/** Properties of a HomeInfo. */
export interface IHomeInfo {

    /** HomeInfo id */
    id?: (string|null);

    /** HomeInfo totalUser */
    totalUser?: (number|null);

    /** HomeInfo onlineUser */
    onlineUser?: (number|null);

    /** HomeInfo todayUser */
    todayUser?: (number|null);

    /** HomeInfo cpu */
    cpu?: (HomeInfo.ICpuInfo|null);

    /** HomeInfo memery */
    memery?: (HomeInfo.IMemeryInfo|null);

    /** HomeInfo disk */
    disk?: (HomeInfo.IDiskInfo[]|null);
}

/** Represents a HomeInfo. */
export class HomeInfo implements IHomeInfo {

    /**
     * Constructs a new HomeInfo.
     * @param [properties] Properties to set
     */
    constructor(properties?: IHomeInfo);

    /** HomeInfo id. */
    public id: string;

    /** HomeInfo totalUser. */
    public totalUser: number;

    /** HomeInfo onlineUser. */
    public onlineUser: number;

    /** HomeInfo todayUser. */
    public todayUser: number;

    /** HomeInfo cpu. */
    public cpu?: (HomeInfo.ICpuInfo|null);

    /** HomeInfo memery. */
    public memery?: (HomeInfo.IMemeryInfo|null);

    /** HomeInfo disk. */
    public disk: HomeInfo.IDiskInfo[];

    /**
     * Creates a new HomeInfo instance using the specified properties.
     * @param [properties] Properties to set
     * @returns HomeInfo instance
     */
    public static create(properties?: IHomeInfo): HomeInfo;

    /**
     * Encodes the specified HomeInfo message. Does not implicitly {@link HomeInfo.verify|verify} messages.
     * @param message HomeInfo message or plain object to encode
     * @param [writer] Writer to encode to
     * @returns Writer
     */
    public static encode(message: IHomeInfo, writer?: $protobuf.Writer): $protobuf.Writer;

    /**
     * Encodes the specified HomeInfo message, length delimited. Does not implicitly {@link HomeInfo.verify|verify} messages.
     * @param message HomeInfo message or plain object to encode
     * @param [writer] Writer to encode to
     * @returns Writer
     */
    public static encodeDelimited(message: IHomeInfo, writer?: $protobuf.Writer): $protobuf.Writer;

    /**
     * Decodes a HomeInfo message from the specified reader or buffer.
     * @param reader Reader or buffer to decode from
     * @param [length] Message length if known beforehand
     * @returns HomeInfo
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): HomeInfo;

    /**
     * Decodes a HomeInfo message from the specified reader or buffer, length delimited.
     * @param reader Reader or buffer to decode from
     * @returns HomeInfo
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): HomeInfo;

    /**
     * Verifies a HomeInfo message.
     * @param message Plain object to verify
     * @returns `null` if valid, otherwise the reason why it is not
     */
    public static verify(message: { [k: string]: any }): (string|null);

    /**
     * Creates a HomeInfo message from a plain object. Also converts values to their respective internal types.
     * @param object Plain object
     * @returns HomeInfo
     */
    public static fromObject(object: { [k: string]: any }): HomeInfo;

    /**
     * Creates a plain object from a HomeInfo message. Also converts values to other types if specified.
     * @param message HomeInfo
     * @param [options] Conversion options
     * @returns Plain object
     */
    public static toObject(message: HomeInfo, options?: $protobuf.IConversionOptions): { [k: string]: any };

    /**
     * Converts this HomeInfo to JSON.
     * @returns JSON object
     */
    public toJSON(): { [k: string]: any };
}

export namespace HomeInfo {

    /** Properties of a CpuInfo. */
    interface ICpuInfo {

        /** CpuInfo system */
        system?: (string|null);

        /** CpuInfo process */
        process?: (string|null);

        /** CpuInfo loadAverage */
        loadAverage?: (string|null);
    }

    /** Represents a CpuInfo. */
    class CpuInfo implements ICpuInfo {

        /**
         * Constructs a new CpuInfo.
         * @param [properties] Properties to set
         */
        constructor(properties?: HomeInfo.ICpuInfo);

        /** CpuInfo system. */
        public system: string;

        /** CpuInfo process. */
        public process: string;

        /** CpuInfo loadAverage. */
        public loadAverage: string;

        /**
         * Creates a new CpuInfo instance using the specified properties.
         * @param [properties] Properties to set
         * @returns CpuInfo instance
         */
        public static create(properties?: HomeInfo.ICpuInfo): HomeInfo.CpuInfo;

        /**
         * Encodes the specified CpuInfo message. Does not implicitly {@link HomeInfo.CpuInfo.verify|verify} messages.
         * @param message CpuInfo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: HomeInfo.ICpuInfo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified CpuInfo message, length delimited. Does not implicitly {@link HomeInfo.CpuInfo.verify|verify} messages.
         * @param message CpuInfo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: HomeInfo.ICpuInfo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a CpuInfo message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns CpuInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): HomeInfo.CpuInfo;

        /**
         * Decodes a CpuInfo message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns CpuInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): HomeInfo.CpuInfo;

        /**
         * Verifies a CpuInfo message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a CpuInfo message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns CpuInfo
         */
        public static fromObject(object: { [k: string]: any }): HomeInfo.CpuInfo;

        /**
         * Creates a plain object from a CpuInfo message. Also converts values to other types if specified.
         * @param message CpuInfo
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: HomeInfo.CpuInfo, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this CpuInfo to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };
    }

    /** Properties of a MemeryInfo. */
    interface IMemeryInfo {

        /** MemeryInfo used */
        used?: (string|null);

        /** MemeryInfo total */
        total?: (string|null);
    }

    /** Represents a MemeryInfo. */
    class MemeryInfo implements IMemeryInfo {

        /**
         * Constructs a new MemeryInfo.
         * @param [properties] Properties to set
         */
        constructor(properties?: HomeInfo.IMemeryInfo);

        /** MemeryInfo used. */
        public used: string;

        /** MemeryInfo total. */
        public total: string;

        /**
         * Creates a new MemeryInfo instance using the specified properties.
         * @param [properties] Properties to set
         * @returns MemeryInfo instance
         */
        public static create(properties?: HomeInfo.IMemeryInfo): HomeInfo.MemeryInfo;

        /**
         * Encodes the specified MemeryInfo message. Does not implicitly {@link HomeInfo.MemeryInfo.verify|verify} messages.
         * @param message MemeryInfo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: HomeInfo.IMemeryInfo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified MemeryInfo message, length delimited. Does not implicitly {@link HomeInfo.MemeryInfo.verify|verify} messages.
         * @param message MemeryInfo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: HomeInfo.IMemeryInfo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a MemeryInfo message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns MemeryInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): HomeInfo.MemeryInfo;

        /**
         * Decodes a MemeryInfo message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns MemeryInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): HomeInfo.MemeryInfo;

        /**
         * Verifies a MemeryInfo message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a MemeryInfo message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns MemeryInfo
         */
        public static fromObject(object: { [k: string]: any }): HomeInfo.MemeryInfo;

        /**
         * Creates a plain object from a MemeryInfo message. Also converts values to other types if specified.
         * @param message MemeryInfo
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: HomeInfo.MemeryInfo, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this MemeryInfo to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };
    }

    /** Properties of a DiskInfo. */
    interface IDiskInfo {

        /** DiskInfo used */
        used?: (string|null);

        /** DiskInfo total */
        total?: (string|null);

        /** DiskInfo name */
        name?: (string|null);
    }

    /** Represents a DiskInfo. */
    class DiskInfo implements IDiskInfo {

        /**
         * Constructs a new DiskInfo.
         * @param [properties] Properties to set
         */
        constructor(properties?: HomeInfo.IDiskInfo);

        /** DiskInfo used. */
        public used: string;

        /** DiskInfo total. */
        public total: string;

        /** DiskInfo name. */
        public name: string;

        /**
         * Creates a new DiskInfo instance using the specified properties.
         * @param [properties] Properties to set
         * @returns DiskInfo instance
         */
        public static create(properties?: HomeInfo.IDiskInfo): HomeInfo.DiskInfo;

        /**
         * Encodes the specified DiskInfo message. Does not implicitly {@link HomeInfo.DiskInfo.verify|verify} messages.
         * @param message DiskInfo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encode(message: HomeInfo.IDiskInfo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Encodes the specified DiskInfo message, length delimited. Does not implicitly {@link HomeInfo.DiskInfo.verify|verify} messages.
         * @param message DiskInfo message or plain object to encode
         * @param [writer] Writer to encode to
         * @returns Writer
         */
        public static encodeDelimited(message: HomeInfo.IDiskInfo, writer?: $protobuf.Writer): $protobuf.Writer;

        /**
         * Decodes a DiskInfo message from the specified reader or buffer.
         * @param reader Reader or buffer to decode from
         * @param [length] Message length if known beforehand
         * @returns DiskInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): HomeInfo.DiskInfo;

        /**
         * Decodes a DiskInfo message from the specified reader or buffer, length delimited.
         * @param reader Reader or buffer to decode from
         * @returns DiskInfo
         * @throws {Error} If the payload is not a reader or valid buffer
         * @throws {$protobuf.util.ProtocolError} If required fields are missing
         */
        public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): HomeInfo.DiskInfo;

        /**
         * Verifies a DiskInfo message.
         * @param message Plain object to verify
         * @returns `null` if valid, otherwise the reason why it is not
         */
        public static verify(message: { [k: string]: any }): (string|null);

        /**
         * Creates a DiskInfo message from a plain object. Also converts values to their respective internal types.
         * @param object Plain object
         * @returns DiskInfo
         */
        public static fromObject(object: { [k: string]: any }): HomeInfo.DiskInfo;

        /**
         * Creates a plain object from a DiskInfo message. Also converts values to other types if specified.
         * @param message DiskInfo
         * @param [options] Conversion options
         * @returns Plain object
         */
        public static toObject(message: HomeInfo.DiskInfo, options?: $protobuf.IConversionOptions): { [k: string]: any };

        /**
         * Converts this DiskInfo to JSON.
         * @returns JSON object
         */
        public toJSON(): { [k: string]: any };
    }
}

/** Properties of a NotificationMessage. */
export interface INotificationMessage {

    /** NotificationMessage id */
    id?: (string|null);

    /** NotificationMessage title */
    title?: (string|null);

    /** NotificationMessage message */
    message?: (string|null);
}

/** Represents a NotificationMessage. */
export class NotificationMessage implements INotificationMessage {

    /**
     * Constructs a new NotificationMessage.
     * @param [properties] Properties to set
     */
    constructor(properties?: INotificationMessage);

    /** NotificationMessage id. */
    public id: string;

    /** NotificationMessage title. */
    public title: string;

    /** NotificationMessage message. */
    public message: string;

    /**
     * Creates a new NotificationMessage instance using the specified properties.
     * @param [properties] Properties to set
     * @returns NotificationMessage instance
     */
    public static create(properties?: INotificationMessage): NotificationMessage;

    /**
     * Encodes the specified NotificationMessage message. Does not implicitly {@link NotificationMessage.verify|verify} messages.
     * @param message NotificationMessage message or plain object to encode
     * @param [writer] Writer to encode to
     * @returns Writer
     */
    public static encode(message: INotificationMessage, writer?: $protobuf.Writer): $protobuf.Writer;

    /**
     * Encodes the specified NotificationMessage message, length delimited. Does not implicitly {@link NotificationMessage.verify|verify} messages.
     * @param message NotificationMessage message or plain object to encode
     * @param [writer] Writer to encode to
     * @returns Writer
     */
    public static encodeDelimited(message: INotificationMessage, writer?: $protobuf.Writer): $protobuf.Writer;

    /**
     * Decodes a NotificationMessage message from the specified reader or buffer.
     * @param reader Reader or buffer to decode from
     * @param [length] Message length if known beforehand
     * @returns NotificationMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    public static decode(reader: ($protobuf.Reader|Uint8Array), length?: number): NotificationMessage;

    /**
     * Decodes a NotificationMessage message from the specified reader or buffer, length delimited.
     * @param reader Reader or buffer to decode from
     * @returns NotificationMessage
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    public static decodeDelimited(reader: ($protobuf.Reader|Uint8Array)): NotificationMessage;

    /**
     * Verifies a NotificationMessage message.
     * @param message Plain object to verify
     * @returns `null` if valid, otherwise the reason why it is not
     */
    public static verify(message: { [k: string]: any }): (string|null);

    /**
     * Creates a NotificationMessage message from a plain object. Also converts values to their respective internal types.
     * @param object Plain object
     * @returns NotificationMessage
     */
    public static fromObject(object: { [k: string]: any }): NotificationMessage;

    /**
     * Creates a plain object from a NotificationMessage message. Also converts values to other types if specified.
     * @param message NotificationMessage
     * @param [options] Conversion options
     * @returns Plain object
     */
    public static toObject(message: NotificationMessage, options?: $protobuf.IConversionOptions): { [k: string]: any };

    /**
     * Converts this NotificationMessage to JSON.
     * @returns JSON object
     */
    public toJSON(): { [k: string]: any };
}
