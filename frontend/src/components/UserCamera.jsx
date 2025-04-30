import { useEffect, useRef, useState } from "react";

const UserCamera = () => {
    const videoRef = useRef(null);
    const streamRef = useRef(null);

    const [isCameraOn, setIsCameraOn] = useState(true);
    const [isMicOn, setIsMicOn] = useState(true);

    useEffect(() => {
        const startMedia = async () => {
            try {
                const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
                streamRef.current = stream;

                if (videoRef.current) {
                    videoRef.current.srcObject = stream;
                }
            } catch (err) {
                console.error("Error accessing webcam/mic:", err);
            }
        };

        startMedia();

        return () => {
            if (streamRef.current) {
                streamRef.current.getTracks().forEach((track) => track.stop());
            }
        };
    }, []);

    const toggleCamera = () => {
        if (!streamRef.current) return;
        const videoTrack = streamRef.current.getVideoTracks()[0];
        if (videoTrack) {
            videoTrack.enabled = !videoTrack.enabled;
            setIsCameraOn(videoTrack.enabled);
        }
    };

    const toggleMic = () => {
        if (!streamRef.current) return;
        const audioTrack = streamRef.current.getAudioTracks()[0];
        if (audioTrack) {
            audioTrack.enabled = !audioTrack.enabled;
            setIsMicOn(audioTrack.enabled);
        }
    };

    return (
        <div className="relative bg-black rounded-lg overflow-hidden w-full max-w-md">
            {isCameraOn ? (
                <video ref={videoRef} autoPlay muted className="w-full h-full object-cover" />
            ) : (
                <div className="flex items-center justify-center h-64 text-white text-xl font-semibold">
                    Vous
                </div>
            )}

            <div className="absolute bottom-2 left-1/2 transform -translate-x-1/2 flex space-x-4">
                <button
                    onClick={toggleCamera}
                    className="px-4 py-1 bg-gray-800 text-white rounded-full hover:bg-gray-700"
                >
                    {isCameraOn ? "Désactiver caméra" : "Activer caméra"}
                </button>
                <button
                    onClick={toggleMic}
                    className="px-4 py-1 bg-gray-800 text-white rounded-full hover:bg-gray-700"
                >
                    {isMicOn ? "Couper micro" : "Activer micro"}
                </button>
            </div>
        </div>
    );
};

export default UserCamera;
