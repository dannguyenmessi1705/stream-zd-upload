import { message } from "antd";
import { useCallback, useRef, useEffect } from "react";

interface MessageQueue {
  type: "success" | "error" | "warning" | "info";
  content: string;
}

export const useMessage = () => {
  const [messageApi, contextHolder] = message.useMessage();
  const messageQueue = useRef<MessageQueue[]>([]);

  useEffect(() => {
    if (messageQueue.current.length > 0) {
      const currentMessage = messageQueue.current[0];
      messageApi[currentMessage.type](currentMessage.content);
      messageQueue.current = messageQueue.current.slice(1);
    }
  }, [messageQueue.current, messageApi]);

  const queueMessage = useCallback(
    (type: MessageQueue["type"], content: string) => {
      messageQueue.current = [...messageQueue.current, { type, content }];
    },
    []
  );

  return {
    message: {
      success: (content: string) => queueMessage("success", content),
      error: (content: string) => queueMessage("error", content),
      warning: (content: string) => queueMessage("warning", content),
      info: (content: string) => queueMessage("info", content),
    },
    contextHolder,
  };
};
