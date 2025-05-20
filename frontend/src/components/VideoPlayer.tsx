import React, { useEffect, useRef } from "react";
import videojs from "video.js";
import "video.js/dist/video-js.css";
import { Video } from "../types/video";
import { videoService } from "../services/videoService";

interface VideoPlayerProps {
  video: Video;
}

const VideoPlayer: React.FC<VideoPlayerProps> = ({ video }) => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const playerRef = useRef<any>(null);

  useEffect(() => {
    if (!videoRef.current) return;

    const hlsUrl = videoService.getHlsUrl(video);
    if (!hlsUrl) return;

    playerRef.current = videojs(videoRef.current, {
      controls: true,
      fluid: true,
      sources: [
        {
          src: hlsUrl,
          type: "application/x-mpegURL",
        },
      ],
    });

    return () => {
      if (playerRef.current) {
        playerRef.current.dispose();
      }
    };
  }, [video]);

  return (
    <div data-vjs-player>
      <video ref={videoRef} className="video-js vjs-big-play-centered" />
    </div>
  );
};

export default VideoPlayer;
